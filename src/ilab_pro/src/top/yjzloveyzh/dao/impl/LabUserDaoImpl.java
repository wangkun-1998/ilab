package top.yjzloveyzh.dao.impl;

import org.springframework.stereotype.Repository;

import top.yjzloveyzh.common.pojo.User;
import top.yjzloveyzh.dao.LabUserDao;
import top.yjzloveyzh.dao.MySqlSessionDaoSupport;


@Repository(value="labUserDaoImpl")
public class LabUserDaoImpl extends MySqlSessionDaoSupport implements LabUserDao {

    @Override
    public long insertLabUser(User user) {

        getSqlSession().getMapper(LabUserDao.class).insertLabUser(user);

        return user.getId();
    }

    @Override
    public User findUserByUserName(String username) {

        return getSqlSession().getMapper(LabUserDao.class).findUserByUserName(username);
    }

    @Override
    public User findUserById(int id) {

        return getSqlSession().getMapper(LabUserDao.class).findUserById(id);
    }
}