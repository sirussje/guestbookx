package com.guest.book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.guest.book.dao.UserDao;
import com.guest.book.entity.USER;
import com.guest.book.entity.USERENTRY;
import com.guest.book.model.UserEntry;
import com.guest.book.model.UserModel;
import com.guest.book.util.HibernateValidatorUtil;

public class UserServiceImpl implements UserService {
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserDao userdao;

	@Override
	public UserModel validateUser(UserModel model) {
		logger.info("index");
		List<USER> status = userdao.validateUser(model);

		if (status.isEmpty()) {
			model.setStatus("failure");
		} else {
			model.setStatus("success");
		}
		logger.info("login Serv End");
		return model;
	}

	@Override
	public UserEntry addUserEntry(UserEntry model) {

		StringJoiner error = new StringJoiner(":");
		Set<ConstraintViolation<UserEntry>> constraintViolations = HibernateValidatorUtil.validatorFactory()
				.validate(model);

		if (constraintViolations.size() > 0) {
			for (ConstraintViolation<UserEntry> violation : constraintViolations) {
									error.add(violation.getPropertyPath() + " " + violation.getMessage());
}
			return model;
		}
		else {
			
			if (userdao.addGuestEntry(model)) {
model.setStatus("success");
				return model;
			}
			else
			{
				model.setStatus("failure");
				return model;
			}
		}
		
	}

	@Override
	public List<UserEntry> pendingadminentrys() {
		List<USERENTRY> entries = userdao.alluserentrys();

		List<UserEntry> list = new ArrayList<>();
		entries.stream().forEach(a -> {
			UserEntry entry = new UserEntry();
			entry.setEntry(a.getENTRY());
			entry.setUser(a.getUSER());
			entry.setId(a.getID());
			list.add(entry);

		});
		return list;
	}

	@Override
	public boolean approveEntry(UserEntry model) {

		return userdao.approveEntry(model);

	}

	@Override
	public boolean removeEntry(UserEntry model) {

		return userdao.removeentry(model);
	}

	@Override
	public UserModel addNewUSer(UserModel model) {
		if (userdao.addNewUserUser(model)) {
model.setStatus("success");
			return model;
		}
		else
		{
			model.setStatus("failure");
			return model;
		}
	}

}
