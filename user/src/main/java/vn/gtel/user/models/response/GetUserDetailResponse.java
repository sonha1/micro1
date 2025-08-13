package vn.gtel.user.models.response;

import java.math.BigDecimal;

public class GetUserDetailResponse {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String fullName;
    private int status;
    private BigDecimal balance;
}
