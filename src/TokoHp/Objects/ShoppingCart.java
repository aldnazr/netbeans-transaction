package TokoHp.Objects;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    List<Product> items = new ArrayList<>();

    public void addItem(Product product) {
        for (Product item : items) {
            if (item.getIdProduk() == product.getIdProduk()) {
                PopUp.errorMessage("Barang sudah ada", "Anda hanya dapat menambah stok di keranjang jika barang sudah ada");
                return;
            }
        }

        items.add(product);
        Toast.toastSuccess("Ditambah ke keranjang");
    }

    public void updateItemQuantity(Product product, int stokBaru) {
        for (Product item : items) {
            if (item.getIdProduk() == product.getIdProduk()) {
                item.setStok(stokBaru);
                Toast.toastSuccess("Jumlah barang diperbarui");
                return;
            }
        }

        PopUp.errorMessage("Barang tidak ada", "Silahkan tambah barang terlebih dahulu");
    }

    public void removeItem(Product product) {
        for (Product item : items) {
            if (product.getIdProduk() == item.getIdProduk()) {
                items.remove(item);
                return;
            }
        }
    }

    public List<Product> getItems() {
        return items;
    }

    public int calculateTotal() {
        int total = 0;

        for (Product item : items) {
            total += item.getHarga() * item.getStok();
        }
        return total;
    }
}
