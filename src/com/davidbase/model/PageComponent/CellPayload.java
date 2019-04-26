package com.davidbase.model.PageComponent;

import com.davidbase.model.DavidBaseError;
import com.davidbase.utils.DataType;
import com.davidbase.utils.DavisBaseUtil;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * This class represents the Data Cell payload
 */
public class CellPayload {

    // 1-byte TINYINT that indicates the number of columns n.
    private byte num_columns;

    // n-bytes which are Serial Type Codes, one for each of n columns
    private byte[] data_type;

    // binary column data. Unlike SQLite, NULL values occupy 1, 2, 4, or 8 bytes
    private byte[] data;

    // not serializable
    private List<Object> colValues;
    private List<DataType> colTypes;
    private short dataSize;

    public CellPayload() {
    }

    public CellPayload(byte num_columns, byte[] data_type) {
        this.num_columns = num_columns;
        this.data_type = data_type;
    }

    public CellPayload(byte num_columns, byte[] data_type, byte[] data) {
        this.num_columns = num_columns;
        this.data_type = data_type;
        this.data = data;
    }

    public byte getNum_columns() {
        return num_columns;
    }

    public void setNum_columns(byte num_columns) {
        this.num_columns = num_columns;
    }

    public byte[] getData_type() {
        return data_type;
    }

    public void setData_type(byte[] data_type) {
        this.data_type = data_type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public List<Object> getColValues() {
        return colValues;
    }

    public void setColValues(List<Object> colValues) {
        this.colValues = colValues;
    }

    public List<DataType> getColTypes() {
        return colTypes;
    }

    public void setColTypes(List<DataType> colTypes) {
        this.colTypes = colTypes;
    }

    public void generateByteData() {
        this.data_type = new byte[colTypes.size()];
        fillSizeArray();
        this.num_columns =(byte) colTypes.size();

        this.data = new byte[dataSize];
        fillDataArray();
    }

    public void fillSizeArray() {
        for (int i = 0; i < colTypes.size(); i++) {
            if (colTypes.get(i) == DataType.TEXT) {
                int textLen = (colValues.get(i) != null ? String.valueOf(colValues.get(i)).length() : 0);
                this.data_type[i] = (byte) (colTypes.get(i).getSerialCode()+textLen);
                this.dataSize += textLen;
            } else {
                this.data_type[i] = colTypes.get(i).getSerialCode();
                this.dataSize += this.colTypes.get(i).getSize();
            }
        }
    }
    private static byte[] intToBytes(final int data) {
        return new byte[] {
                (byte)((data >> 24) & 0xff),
                (byte)((data >> 16) & 0xff),
                (byte)((data >> 8) & 0xff),
                (byte)((data >> 0) & 0xff),
        };
    }

    private static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    private static byte[] floatToBytes(final float data) {
        int intBits =  Float.floatToIntBits(data);
        return new byte[] {
                (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };
    }

    private void fillDataArray() {
        int i = 0;
        int k = 0;
        for (DataType type : colTypes)
            switch (type) {
            case TEXT:
                for (byte valueInBytes : String.valueOf(colValues.get(k++)).getBytes())
                {
                    this.data[i++] = valueInBytes;
                }
                break;
            case INT:
                for ( byte valueInBytes: intToBytes(Integer.valueOf(String.valueOf(colValues.get(k++)))))
                {
                    this.data[i++] = valueInBytes;
                }
                    break;
                case TINYINT:
                case YEAR:
                for ( byte valueInBytes: intToBytes(Byte.valueOf(String.valueOf(colValues.get(k++)))))
                {
                    this.data[i++] = valueInBytes;
                }
                break;
                case SMALLINT:
                for ( byte valueInBytes: intToBytes(Integer.valueOf(String.valueOf(colValues.get(k++)))))
                {
                    this.data[i++] = valueInBytes;
                }
                break;
            case REAL:
                for ( byte valueInBytes: floatToBytes(Float.valueOf(String.valueOf(colValues.get(k++)))))
                {
                    this.data[i++] = valueInBytes;
                }
                break;
                case TIME:
                    for ( byte valueInBytes: intToBytes(Integer.valueOf(String.valueOf(colValues.get(k++)))))
                    {
                        this.data[i++] = valueInBytes;
                    }
                    break;
                case DATE:
                    LocalDate valDate = DavisBaseUtil.getValidDate(String.valueOf(colValues.get(k++)));
                    for ( byte valueInBytes: longToBytes(valDate.toEpochSecond(LocalTime.MIN,ZoneOffset.UTC)))
                    {
                        this.data[i++] = valueInBytes;
                    }
                    break;
                case DATETIME:
                    LocalDateTime valDateTime = DavisBaseUtil.getValidDateTime(String.valueOf(colValues.get(k++)));
                    for ( byte valueInBytes: longToBytes(valDateTime.toEpochSecond(ZoneOffset.UTC))) {
                        this.data[i++] = valueInBytes;
                    }
                    break;
                default:
                throw new DavidBaseError("Unable to write data type");
            }
    }

    public short getPayloadSize() {
        int payloadSize =  Byte.BYTES+ (Byte.BYTES)* (data_type.length) + (Byte.BYTES)* (data.length);
        return (short)payloadSize;
    }

    public void setDataSize(short dataSize) {
        this.dataSize = dataSize;
    }

}
