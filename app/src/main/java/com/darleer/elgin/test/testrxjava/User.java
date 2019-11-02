package com.darleer.elgin.test.testrxjava;

import java.util.List;

public class User {
    public String userName;
    public List<Address> AddressList;

    public static class Address
    {
        public String street;
        public String city;

        /**
         * 初始化地址
         * @param s 街道
         * @param c 城市
         */
        public Address(String s,String c)
        {
            street = s;
            city = c;
        }
    }
}
