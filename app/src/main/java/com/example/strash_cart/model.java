package com.example.strash_cart;

public class model {

        String Description, Location, Type, email;

        public model() {
        }

        public model(String Description, String Location, String Type,String email) {
            this.Description = Description;
            this.email = email;
            this.Location = Location;
            this.Type = Type;
        }

        public String getDescription() {
            return Description;
        }

        public String getLocation() {
            return Location;
        }

        public String getType() {
            return Type;
        }

        public String getEmail() {
            return email;
        }

//        public void set(String name) {
//            this.name = name;
//        }

    }
