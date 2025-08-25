package com.gtel.warehouse.utils;

public interface CONSTANT {

    interface API_KEY_STATUS{
        int ACTIVE = 1;
        int INACTIVE = 0;
    }


    interface APPLICATION_STATUS{
        int ACTIVE = 1;
        int INACTIVE = 0;
    }

    class HEADER {
        public static final String TOKEN_PREFIX = "bearer ";
        public static final String HEADER_TOKEN = "Authorization";
    }

    class OTT_APP {
        public static final String ZALO = "ZALO";
        public static final String TELE = "TELE";
        public static final String SMS = "SMS";
        public static final String LINE = "LINE";
        public static final String FACEBOOK = "FACEBOOK";
    }

}
