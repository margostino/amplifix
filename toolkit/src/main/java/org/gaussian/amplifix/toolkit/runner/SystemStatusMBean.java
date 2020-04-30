package org.gaussian.amplifix.toolkit.runner;

public interface SystemStatusMBean {

    Integer getNumberOfSecondsRunning();

    String getProgramName();

    Long getNumberOfUnixSecondsRunning();

    Boolean getSwitchStatus();

}