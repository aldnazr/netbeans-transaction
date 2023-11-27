package TokoHp.Function;

public class Product {

    int idProduk;
    String NamaProduk;
    int Stok;
    int Harga;

    public Product(int idProduk, String NamaProduk, int Stok, int Harga) {
        this.idProduk = idProduk;
        this.NamaProduk = NamaProduk;
        this.Stok = Stok;
        this.Harga = Harga;
    }

    public void setHarga(int Harga) {
        this.Harga = Harga;
    }

    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
    }

    public void setNamaProduk(String NamaProduk) {
        this.NamaProduk = NamaProduk;
    }

    public void setStok(int Stok) {
        this.Stok = Stok;
    }

    public int getHarga() {
        return Harga;
    }

    public int getIdProduk() {
        return idProduk;
    }

    public String getNamaProduk() {
        return NamaProduk;
    }

    public int getStok() {
        return Stok;
    }

}
