package com.github.wojhan.epfuel;

import com.google.gson.annotations.SerializedName;

class GasStation {
    @SerializedName("name")
    private String name;

    @SerializedName("geometry")
    Geometry geometry;

    @SerializedName("icon")
    String icon;

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    class Geometry {
        @SerializedName("location")
        Location location;

        public Location getLocation() {
            return location;
        }

        class Location {
            @SerializedName("lat")
            double lat;

            @SerializedName("lng")
            double lng;

            public double getLat() {
                return lat;
            }

            public double getLng() {
                return lng;
            }
        }
    }
}
