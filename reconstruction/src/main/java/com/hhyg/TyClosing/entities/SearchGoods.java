package com.hhyg.TyClosing.entities;

import java.util.List;

/**
 * Created by user on 2017/6/9.
 */

public class SearchGoods {

    /**
     * errcode : 1
     * op : searchgoods
     * channel : 渠道码
     * imei : 98bce97b104a4993
     * msg : 当errcode不为1时，此处有提示信息
     * data : {"goodsList":[{"barcode":"3346470418059","name":"亲亲唇膏343 3.5g","brand_name":"GUERLAIN娇兰","price":220,"mianshui_price":220,"market_price":320,"image":"https://img.mianshui365.com/upload/20151229/19211045409.jpg@400h_400w_95q_1wh","stock":88,"active_detail":"","active_type":0,"active_code":"","is_privileged":false}],"pageNo":1,"pageSize":24,"totalRows":121,"totalPages":6,"firstPage":true,"lastPage":false,"searchKey":"唇膏,该字段为了兼容同义词而设置，用于搜索过滤条件回传关键词"}
     */

    private String errcode;
    private String op;
    private String channel;
    private String imei;
    private String msg;
    private DataBean data;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * goodsList : [{"barcode":"3346470418059","name":"亲亲唇膏343 3.5g","brand_name":"GUERLAIN娇兰","price":220,"mianshui_price":220,"market_price":320,"image":"https://img.mianshui365.com/upload/20151229/19211045409.jpg@400h_400w_95q_1wh","stock":88,"active_detail":"","active_type":0,"active_code":"","is_privileged":false}]
         * pageNo : 1
         * pageSize : 24
         * totalRows : 121
         * totalPages : 6
         * firstPage : true
         * lastPage : false
         * searchKey : 唇膏,该字段为了兼容同义词而设置，用于搜索过滤条件回传关键词
         */

        private int pageNo;
        private int pageSize;
        private int totalRows;
        private int totalPages;
        private boolean firstPage;
        private boolean lastPage;
        private String searchKey;
        private List<GoodsListBean> goodsList;

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public String getSearchKey() {
            return searchKey;
        }

        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public static class GoodsListBean {
            /**
             * barcode : 3346470418059
             * name : 亲亲唇膏343 3.5g
             * brand_name : GUERLAIN娇兰
             * price : 220
             * mianshui_price : 220
             * market_price : 320
             * image : https://img.mianshui365.com/upload/20151229/19211045409.jpg@400h_400w_95q_1wh
             * stock : 88
             * active_detail :
             * active_type : 0
             * active_code :
             * is_privileged : false
             */

            private String barcode;
            private String name;
            private String brand_name;
            private String price;
            private String mianshui_price;
            private String market_price;
            private String image;
            private int stock;
            private String active_detail;
            private int active_type;
            private String active_code;
            private boolean is_privileged;

            public String getBarcode() {
                return barcode;
            }

            public void setBarcode(String barcode) {
                this.barcode = barcode;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMianshui_price() {
                return mianshui_price;
            }

            public void setMianshui_price(String mianshui_price) {
                this.mianshui_price = mianshui_price;
            }

            public String getMarket_price() {
                return market_price;
            }

            public void setMarket_price(String market_price) {
                this.market_price = market_price;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public String getActive_detail() {
                return active_detail;
            }

            public void setActive_detail(String active_detail) {
                this.active_detail = active_detail;
            }

            public int getActive_type() {
                return active_type;
            }

            public void setActive_type(int active_type) {
                this.active_type = active_type;
            }

            public String getActive_code() {
                return active_code;
            }

            public void setActive_code(String active_code) {
                this.active_code = active_code;
            }

            public boolean isIs_privileged() {
                return is_privileged;
            }

            public void setIs_privileged(boolean is_privileged) {
                this.is_privileged = is_privileged;
            }
        }
    }
}
