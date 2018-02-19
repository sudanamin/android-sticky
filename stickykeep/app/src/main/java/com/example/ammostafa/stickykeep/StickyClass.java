package com.example.ammostafa.stickykeep;

/**
 * Created by Aminov on 2/9/2018.
 */




    public class StickyClass {
        private String sdata;
        private String title;
        private String company;
        private String image;

        public StickyClass() {
        }

        public StickyClass(String data, String title, String company, String image) {
            this.sdata = data;
            this.title = title;
            this.company = company;
            this.image = image;
        }

        public String getData() {
            return sdata;
        }

        public void setData(String data) {
            this.sdata = data;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
