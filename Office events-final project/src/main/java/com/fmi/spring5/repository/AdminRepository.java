package com.fmi.spring5.repository;

import com.fmi.spring5.model.Admin;
import com.fmi.spring5.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Admin, String> {

    void deleteAdminByUser(User user);
}
