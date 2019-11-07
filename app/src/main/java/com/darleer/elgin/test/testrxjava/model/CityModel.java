package com.darleer.elgin.test.testrxjava.model;

import java.util.List;

public class CityModel {

    /**
     * city : 珠海
     * stations : [{"station_name":"吉大","station_code":"1367A"},
     * {"station_name":"前山","station_code":"1368A"},
     * {"station_name":"唐家","station_code":"1369A"},
     * {"station_name":"斗门","station_code":"1370A"}]
     */

    private String city;
    private List<StationsBean> stations;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<StationsBean> getStations() {
        return stations;
    }

    public void setStations(List<StationsBean> stations) {
        this.stations = stations;
    }

    public static class StationsBean {
        /**
         * station_name : 吉大
         * station_code : 1367A
         */

        private String station_name;
        private String station_code;

        public String getStation_name() {
            return station_name;
        }

        public void setStation_name(String station_name) {
            this.station_name = station_name;
        }

        public String getStation_code() {
            return station_code;
        }

        public void setStation_code(String station_code) {
            this.station_code = station_code;
        }
    }
}
