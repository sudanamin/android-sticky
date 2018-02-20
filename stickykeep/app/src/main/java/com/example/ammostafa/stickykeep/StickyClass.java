package com.example.ammostafa.stickykeep;

/**
 * Created by Aminov on 2/9/2018.
 */




    public class StickyClass {
        private String Scolor;

        private int  top;
        private int left;
    private String sdata ;

        public StickyClass() {
        }

      /*  public StickyClass(String sdata, String Scolor, int  top, int left) {
            this.sdata = sdata;
            this.Scolor = Scolor;
            this.top = top;
            this.left = left;
        }*/

        public String getdata() {
            return sdata;
        }

          public int getTop() {
            return this.top;
        }


        public String getScolor() {
            return Scolor;
        }
/*
        public void setCompany(String company) {
            this.company = company;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }*/
    }
