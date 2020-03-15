package r.sardorbek.mytaxiapp;


public class Data {
    private String startingLocation, finalLocation, price;

    Data(String startingLocation, String finalLocation, String price) {
        this.startingLocation = startingLocation;
        this.finalLocation = finalLocation;
        this.price = price;
    }

    public String getStartingLocations() {
        return startingLocation;
    }

    public String getFinalLocation() {
        return finalLocation;
    }

    public String getPrice() {
        return price;
    }
}

