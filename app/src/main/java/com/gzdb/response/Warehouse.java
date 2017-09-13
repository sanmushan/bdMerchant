package com.gzdb.response;

/**
 * Created by zhumg on 4/21.
 */
public class Warehouse implements java.io.Serializable {

    private String formatAddress;
    private long warehouseId;
    private String name;
    private long passportId;

    private boolean select = false;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getFormatAddress() {
        return formatAddress;
    }

    public void setFormatAddress(String formatAddress) {
        this.formatAddress = formatAddress;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPassportId() {
        return passportId;
    }

    public void setPassportId(long passportId) {
        this.passportId = passportId;
    }
}
