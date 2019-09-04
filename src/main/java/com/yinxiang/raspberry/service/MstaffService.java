package com.yinxiang.raspberry.service;

import com.yinxiang.raspberry.mapper.MstaffMapper;
import com.yinxiang.raspberry.model.Mstaff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MstaffService implements UserDetailsService {
    @Autowired
    MstaffMapper mstaffMapper;

    @Override
    public UserDetails loadUserByUsername(String LoginName) throws UsernameNotFoundException {
        Mstaff mstaff = mstaffMapper.loadUserByUsername(LoginName);
        if (mstaff == null) {
            throw new UsernameNotFoundException("账户不存在!");
        }

        return mstaff;
    }
}
