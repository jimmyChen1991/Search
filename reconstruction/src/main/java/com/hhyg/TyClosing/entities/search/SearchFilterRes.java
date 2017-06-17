package com.hhyg.TyClosing.entities.search;

import java.util.List;

/**
 * Created by chenqiyang on 17/6/11.
 */

public class SearchFilterRes {


    /**
     * errcode : 1
     * op : emptyop
     * data : {"class2List":[{"name":"Dior迪奥","id":14}],"propertyList":[{"name":"商品类型","value":["粉底液","粉底霜"]},{"name":"功效","value":["均匀肤色","保湿","防晒","控油","提亮肤色","遮瑕","滋润","隔离","平衡","提拉紧致"]}],"brandList":[{"num":34,"name":"BOBBI BROWN芭比波朗","id":189},{"num":28,"name":"ESTÉE LAUDER雅诗兰黛","id":80},{"num":20,"name":"GIORGIO ARMANI乔治·阿玛尼","id":18},{"num":18,"name":"CLINIQUE倩碧","id":81},{"num":17,"name":"Lancôme兰蔻","id":79},{"num":16,"name":"SHU UEMURA植村秀","id":183},{"num":15,"name":"GUERLAIN娇兰","id":85},{"num":11,"name":"BENEFIT贝玲妃","id":184},{"num":3,"name":"SHISEIDO资生堂","id":125},{"num":2,"name":"HELENA RUBINSTEIN赫莲娜","id":190},{"num":2,"name":"Missha谜尚","id":198},{"num":2,"name":"WHOO 后","id":237}],"priceList":[{"minPrice":0,"maxPrice":99},{"minPrice":100,"maxPrice":299},{"minPrice":300,"maxPrice":499},{"minPrice":500,"maxPrice":999},{"minPrice":1000,"maxPrice":1999}],"class3List":[{"name":"Dior迪奥","id":14}]}
     * channel : 673
     * msg : 成功
     */

    private int errcode;
    private String op;
    private DataBean data;
    private String channel;
    private String msg;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<Class2ListBean> class2List;
        private List<PropertyListBean> propertyList;
        private List<BrandListBean> brandList;
        private List<PriceListBean> priceList;
        private List<Class3ListBean> class3List;

        public List<Class2ListBean> getClass2List() {
            return class2List;
        }

        public void setClass2List(List<Class2ListBean> class2List) {
            this.class2List = class2List;
        }

        public List<PropertyListBean> getPropertyList() {
            return propertyList;
        }

        public void setPropertyList(List<PropertyListBean> propertyList) {
            this.propertyList = propertyList;
        }

        public List<BrandListBean> getBrandList() {
            return brandList;
        }

        public void setBrandList(List<BrandListBean> brandList) {
            this.brandList = brandList;
        }

        public List<PriceListBean> getPriceList() {
            return priceList;
        }

        public void setPriceList(List<PriceListBean> priceList) {
            this.priceList = priceList;
        }

        public List<Class3ListBean> getClass3List() {
            return class3List;
        }

        public void setClass3List(List<Class3ListBean> class3List) {
            this.class3List = class3List;
        }

        public static class Class2ListBean {
            /**
             * name : Dior迪奥
             * id : 14
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class BrandListBean {
            /**
             * num : 34
             * name : BOBBI BROWN芭比波朗
             * id : 189
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class PriceListBean {
            /**
             * minPrice : 0
             * maxPrice : 99
             */

            private String minPrice;
            private String maxPrice;

            public String getMinPrice() {
                return minPrice;
            }

            public void setMinPrice(String minPrice) {
                this.minPrice = minPrice;
            }

            public String getMaxPrice() {
                return maxPrice;
            }

            public void setMaxPrice(String maxPrice) {
                this.maxPrice = maxPrice;
            }
        }

        public static class Class3ListBean {
            /**
             * name : Dior迪奥
             * id : 14
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
