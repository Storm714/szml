package com.storm.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogEvent {

    private Long id;
    private String action;
    private String ip;
    private String detail;

    public static OperationLogEvent of(Long id, String action, String ip, String detail) {
        return new OperationLogEvent(id, action, ip, detail);
    }
}
