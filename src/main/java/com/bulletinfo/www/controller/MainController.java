package com.bulletinfo.www.controller;

import com.bulletinfo.www.domain.*;
import com.bulletinfo.www.servers.*;
import com.bulletinfo.www.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    private FServers fServers;

    @Autowired
    private UserServers userServers;

    @Autowired
    private UserMServers userMServers;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GMService gmService;

    @PostMapping(value = "/")
    public String MainTest(){
        return "hello";
    }

    /**
     * 注册账号，自动生成uid
     * @param user
     * @return
     * @throws SQLException
     */
    @PostMapping("/reginster")
    public Result Register(@Valid User user){
        System.out.println("user is:"+user);
        userServers.AddUser(user);
        return ResultUtils.success(null);
    }

    /**
     * 遍历uid的好友
     * @param uid
     * @return
     */
    @PostMapping("/friends/{uid}")
    public Result FindByFriend(@PathVariable Integer uid){
        List<Friend> list = fServers.UserList(uid);
        return ResultUtils.success(list);
    }

    /**
     * 登录判断
     * @param uid
     * @param upwd
     * @return
     */
    @PostMapping("/login/{uid}/{upwd}")
    public Result Login(@PathVariable Integer uid, @PathVariable String upwd){
        boolean result= userServers.Login(uid, upwd);
        return ResultUtils.success(result);
    }

    /**
     * 发送消息
     * @param userMessage
     * @return
     */
    @PostMapping("/sendmsg")
    public Result SendMsg(@Valid UserMessage userMessage){
        userMServers.SendMsg(userMessage);
        return ResultUtils.success(null);
    }

    /**
     * 查询好友间的聊天信息，接收消息
     * @param mid
     * @param uid
     * @return
     */
    @PostMapping("/receive/{mid}/{uid}")
    public Result ReciveMsg(@PathVariable Integer mid, @PathVariable Integer uid){
        List<UserMessage> list = userMServers.ReceiveMsg(mid, uid);
        return ResultUtils.success(list);
    }

    /**
     * 创建群，自动生成gid
     * @param groups
     * @return
     */
    @PostMapping("/createGroup")
    public Result CreateGroup(@Valid Groups groups){
        groupService.CreateGroup(groups);
        return ResultUtils.success(null);
    }

    /**
     * 查询群成员信息
     * @param gid
     * @return
     */
    @PostMapping("/selectGPersons/{gid}")
    public Result SelectGroupsPerson(@PathVariable Integer gid){
        List list = groupService.SelectGPersons(gid);
        List pLists = new ArrayList();
        for(Object li : list){
            pLists.add(userServers.SelectUInfo(Integer.valueOf(String.valueOf(li))));
        }
        return ResultUtils.success(pLists);
    }

    /**
     * 发送群消息,记得需要群id
     * @param groupMessage
     * @return
     */
    @PostMapping("/SendGMsg")
    public Result SendGMsg(@Valid GroupMessage groupMessage){
        gmService.SendGMsg(groupMessage);
        return ResultUtils.success(null);
    }

    /**
     * 更新所有的群消息
     * @param uid
     * @return
     */
    @PostMapping("/reciveGmsg/{uid}")
    public Result ReceiveGmsg(@PathVariable Integer uid){
        List lists = userServers.SelectGidLists(uid);
        List glists = new ArrayList();
        for(Object li : lists){
            glists.add(gmService.SelectGidMsg(Integer.valueOf(String.valueOf(li))));
        }
        return ResultUtils.success(glists);
    }


}
