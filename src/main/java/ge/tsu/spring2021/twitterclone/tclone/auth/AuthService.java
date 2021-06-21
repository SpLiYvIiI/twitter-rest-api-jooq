package ge.tsu.spring2021.twitterclone.tclone.auth;

import ge.tsu.spring2021.twitterclone.tclone.user.User;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.InvalidCredentialsException;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordAlreadyExistsException;
import ge.tsu.spring2021.twitterclone.tclone.utils.exceptions.RecordNotFoundException;
import nu.studer.sample.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    DSLContext dslContext;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public boolean login(LoginView user) throws RecordNotFoundException{
        int c = dslContext.selectCount()
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(user.getUserName()))
                .fetchOne()
                .into(int.class);
        if(c != 0) {
            User findUser = dslContext.selectFrom(Tables.USERS)
                    .where(Tables.USERS.USER_NAME.equal(user.getUserName()))
                    .fetchOne()
                    .into(User.class);
            if(bCryptPasswordEncoder.matches(user.getPassword(),findUser.getPassword()))
                return true;
            else return false;
        }
        else throw new RecordNotFoundException("User with such username doesnt exist");
    }
    public void register(RegisterView newUser) throws RecordAlreadyExistsException, InvalidCredentialsException {
        if(newUser.getUserName().equals("") || newUser.getEmail().equals("") || newUser.getPassword().equals("")){
            throw new InvalidCredentialsException("Wrong data");
        }
        int c = dslContext.selectCount()
                .from(Tables.USERS)
                .where(Tables.USERS.USER_NAME.equal(newUser.getUserName()))
                .or(Tables.USERS.EMAIL.equal(newUser.getEmail()))
                .fetchOne()
                .into(int.class);
        if(c != 0)
        {
            throw new RecordAlreadyExistsException("User with such email or username already exists");
        }
        else{
            System.out.println(bCryptPasswordEncoder.encode(newUser.getPassword()));
        }
    }
}
