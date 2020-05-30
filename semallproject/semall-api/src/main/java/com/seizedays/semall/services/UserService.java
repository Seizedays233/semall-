package com.seizedays.semall.services;

import com.seizedays.semall.beans.UmsMember;
import com.seizedays.semall.beans.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUSer();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(Long memberId);


    UmsMember login(UmsMember umsMember);

    void addUserToken(String token, Long memberId);

    void addUser(UmsMember umsMember);

    UmsMember getUserBySourceUid(Long sourceUid);

    UmsMemberReceiveAddress getReceiveAddressByAddressId(String receiveAddressId);
}
