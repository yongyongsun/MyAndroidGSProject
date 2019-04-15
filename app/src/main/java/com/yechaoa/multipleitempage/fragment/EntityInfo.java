package com.yechaoa.multipleitempage.fragment;

import java.util.List;

/**
 * @author syy
 */
public class EntityInfo {


    /**
     * reason : 请求成功
     {
     "code": "200",
     "message": "查询工作单列表成功",
     "success": true,
     "total": 2
     "rows": [
     {
     "id": "11",
     "year": "2019",
     "goodsClass": "101",
     "sampleName": "电压力锅",
     "operator": "益阳市赫山区水铺镇百佳超市",
     "samplingPerson": "张三",
     "samplingDate": "20190310",
     "workSheetFileSize": "5242880",
     "workSheetPDFurl": " http://127.0.0.1:8080/file/pdf/ff23fde89abc.pdf"
     },
     {
     "id": "12",
     "year": "2019",
     "goodsClass": "101",
     "sampleName": "电风扇",
     "operator": "益阳市赫山区水铺镇百佳超市",
     "samplingPerson": "张三",
     "samplingDate": "20190311",
     "workSheetFileSize": "5242880",
     "workSheetPDFurl": " http://127.0.0.1:8080/file/pdf/ff23fde23aad.pdf"
     }
     ]
     }

     * error_code : 0
     */

    private String code;
    private String message;
    private boolean success;
    private int total;
    private List<ListBean> rows;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getTotal() {
        return total;
    }

    public List<ListBean> getRows() {
        return rows;
    }

    public static class ListBean {
        private String id;
        private String year;
        private String goodsClass;
        private String sampleName;
        private String operator;
        private String samplingPerson;
        private String samplingDate;
        private long   workSheetFileSize;
        private String workSheetPDFurl;

        public String getId() {
            return id;
        }

        public String getYear() {
            return year;
        }

        public String getGoodsClass() {
            return goodsClass;
        }

        public String getSampleName() {
            return sampleName;
        }

        public String getOperator() {
            return operator;
        }

        public String getSamplingPerson() {
            return samplingPerson;
        }

        public String getSamplingDate() {
            return samplingDate;
        }

        public long getWorkSheetFileSize() {
            return workSheetFileSize;
        }

        public String getWorkSheetPDFurl() {
            return workSheetPDFurl;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", year='" + year + '\'' +
                    ", goodsClass='" + goodsClass + '\'' +
                    ", sampleName='" + sampleName + '\'' +
                    ", operator='" + operator + '\'' +
                    ", samplingPerson='" + samplingPerson + '\'' +
                    ", samplingDate='" + samplingDate + '\'' +
                    ", workSheetFileSize='" + workSheetFileSize + '\'' +
                    ", workSheetPDFurl='" + workSheetPDFurl + '\'' +
                    '}';
        }
    }
}
