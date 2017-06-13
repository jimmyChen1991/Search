package com.hhyg.TyClosing.entities.search;

import java.util.List;

/**
 * Created by chenqiyang on 17/6/11.
 */

public class SearchFilterRes {

    private List<PropertyListBean> propertyList;
    private List<BrandListBean> brandList;
    private List<PriceListBean> priceList;
    private List<Class2ListBean> class2List;
    private List<Class3ListBean> class3List;

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

    public List<Class2ListBean> getClass2List() {
        return class2List;
    }

    public void setClass2List(List<Class2ListBean> class2List) {
        this.class2List = class2List;
    }

    public List<Class3ListBean> getClass3List() {
        return class3List;
    }

    public void setClass3List(List<Class3ListBean> class3List) {
        this.class3List = class3List;
    }

    public static class PropertyListBean {
        /**
         * name : 功效
         * value : ["滋润","持久","保湿"]
         */

        private String name;
        private List<String> value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }

    public static class BrandListBean {
        /**
         * num : 85
         * name : Dior迪奥
         * id : 14
         * rukou : 0
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

    public static class Class2ListBean {
        /**
         * num : 85
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

    public static class Class3ListBean {
        /**
         * num : 85
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
