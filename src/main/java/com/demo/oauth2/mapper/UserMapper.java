package com.demo.oauth2.mapper;

import java.util.List;

import com.demo.oauth2.entity.User;

public interface UserMapper {
	public User createUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long userId);

    User findOne(Long userId);

    List<User> findAll();

    User findByUsername(String username);
}
