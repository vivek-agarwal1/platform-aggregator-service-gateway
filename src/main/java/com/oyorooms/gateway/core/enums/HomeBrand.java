package com.oyorooms.gateway.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HomeBrand {
    ADS("ADS", "Admiral Strand"),
    AR("AR", "Ardennes Relais"),
    ATL("ATL", "@Leisure"),
    AZ("AZ", "Aan Zee"),
    BEC("BEC", "Belvilla Elite Collection"),
    BV("BV", "Belvilla"),
    DC("DC", "DanCenter"),
    DL("DL", "Danland"),
    OHMS("OHMS", "OYO Homes"),
    STS("STS", "Stugsommar"),
    TF("TF", "TUI Ferienhaus"),
    TT("TT", "Topic Travel"),
    VXL("VXL", "VillaXL");

    @JsonValue
    private final String brandCode;

    private final String brandName;

    @Override
    public String toString() {
        return this.brandCode;
    }

}
