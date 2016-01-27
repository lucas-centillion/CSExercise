package com.semjournals.shiro;

import com.semjournals.data.dao.AccountDAO;
import com.semjournals.model.Account;
import com.semjournals.model.Role;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Shiro Realm used for authentication and authorization, based on JdbcRealm.
 * It connects to the database using the appropriated services in order to validate users's credentials and roles.
 */
public class SEMJournalsJdbcRealm extends JdbcRealm {

	private static final Logger LOG = LoggerFactory.getLogger(SEMJournalsJdbcRealm.class);

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();

		if (username == null) {
			String message = "Null usernames are not allowed by this realm";
			LOG.error(message);
			throw new AccountException(message);
		}

		Account account = new AccountDAO().get("email", username);

		if (account == null) {
			String message = "No account found for user [" + username + "]";
			LOG.error(message);
			throw new UnknownAccountException("No account found for user [" + username + "]");
		}

		String password = account.getPassword();
		ByteSource salt = ByteSource.Util.bytes(account.getSalt());

		return new SimpleAuthenticationInfo(username, password, salt, getName());
	}

	@Override
	public CredentialsMatcher getCredentialsMatcher() {
		return new SEMJournalsCredentialMatcher();
	}


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		}

		String username = (String) getAvailablePrincipal(principals);
		Account account = new AccountDAO().get("email", username);

		Role role = account.getRole();

		if (role == null) {
			String message = "No role found for user [" + username + "]";
			LOG.error(message);
			throw new UnknownAccountException("No role found for user [" + username + "]");
		}

		Set<String> roleNames = new HashSet<>();
		roleNames.add(role.getName());
		return new SimpleAuthorizationInfo(roleNames);
	}
}