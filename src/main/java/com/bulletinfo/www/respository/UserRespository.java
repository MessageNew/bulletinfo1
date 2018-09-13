package com.bulletinfo.www.respository;

import com.bulletinfo.www.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Mysteriouseyes on 2018/9/5.
 */
public interface UserRespository extends JpaRepository<User, Long> {

     public User findByUidAndPassword(Integer uid, String upwd);
     public User findByUid(Integer uid);

     @Modifying
     @Query("update User u set u.gidList = ?1 where u.uid = ?2")
     public void UpdateGidList(String glist, Integer uid);

}
