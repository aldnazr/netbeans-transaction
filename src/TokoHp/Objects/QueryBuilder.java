package TokoHp.Objects;

public class QueryBuilder {

    public static String riwayatTransaksi(String queryTanggal) {
        String query = """
               SELECT T.ID_TRANSAKSI,
               USR.NAMA_LENGKAP AS PELAYAN,
               T.NAMA_PELANGGAN,
               T.TANGGAL,
               BR.NAMA_BRAND,
               PH.NAMA_HANDPHONE,
               DT.HARGA_BARANG,
               DT.JUMLAH_PEMBELIAN,
               DT.SUBTOTAL,
               T.TOTAL_HARGA
               FROM TRANSAKSI T
                       JOIN DETAIL_TRANSAKSI DT ON T.ID_TRANSAKSI = DT.ID_TRANSAKSI JOIN USERS USR ON T.ID_USER = USR.ID_USER JOIN PHONES PH ON DT.ID_PHONE = PH.ID_PHONE JOIN BRAND BR ON PH.ID_BRAND = BR.ID_BRAND
                       WHERE ${queryTanggal} (T.ID_TRANSAKSI LIKE ? OR LOWER(USR.NAMA_LENGKAP) LIKE ? OR LOWER(T.NAMA_PELANGGAN) LIKE ? OR TO_CHAR(T.TANGGAL, 'YYYY-MM-DD HH24:MI:SS') LIKE ?)
                       ORDER BY T.ID_TRANSAKSI DESC""";

        return query.replace("${queryTanggal}", queryTanggal);
    }

    public static String daftarKaryawan(String tipeAkun) {
        String query = """
                    SELECT ID_USER ,
                    NAMA_LENGKAP ,
                    TANGGAL_LAHIR ,
                    GENDER ,
                    ALAMAT ,
                    EMAIL ,
                    NO_TELP ,
                    TIPE_AKUN ,
                    USERNAME FROM USERS WHERE ${tipeAkun} (LOWER(NAMA_LENGKAP) LIKE ?)""";

        return query.replace("${tipeAkun}", tipeAkun);
    }

    public static String tableDaftarHP() {
        return """
               SELECT
               ID_PHONE,
               NAMA_BRAND,
               NAMA_HANDPHONE,
               STOK,
               HARGA FROM PHONES
               JOIN BRAND USING (ID_BRAND)
               WHERE STOK > 0 AND LOWER(NAMA_BRAND) LIKE ? OR LOWER(NAMA_HANDPHONE) LIKE ?""";
    }

    public static String filterPhone(String filteredQuery) {
        String query = """
                       SELECT
                       ID_PHONE,
                       NAMA_BRAND,
                       NAMA_HANDPHONE,
                       DESKRIPSI,
                       HARGA,
                       STOK FROM PHONES
                       JOIN BRAND USING (ID_BRAND)
                       WHERE ${filteredQuery} (LOWER(NAMA_HANDPHONE) LIKE ? OR LOWER(NAMA_BRAND) LIKE ?)""";

        return query.replace("${filteredQuery}", filteredQuery);
    }

    public static String updateUserProfile(Boolean includePassword) {
        String sql = """
                     UPDATE USERS
                     SET NAMA_LENGKAP = ?,
                     TANGGAL_LAHIR = ?,
                     GENDER = ?,
                     ALAMAT = ?,
                     EMAIL = ?,
                     NO_TELP = ?,
                     USERNAME = ?, PASSWORD = ?
                     WHERE ID_USER = ?""";

        return includePassword ? sql : sql.replace(", PASSWORD = ?", " ");
    }

    public static String updateKaryawan(Boolean isWithPassword) {
        String query = """
                    UPDATE USERS SET NAMA_LENGKAP=?,
                    TANGGAL_LAHIR=?,
                    GENDER=?,
                    ALAMAT=?,
                    EMAIL=?,
                    NO_TELP=?,
                    TIPE_AKUN=?,
                    USERNAME=?, PASSWORD=?
                    WHERE ID_USER=?""";

        return isWithPassword ? query : query.replace(", PASSWORD=?", " ");
    }

    public static String tambahKaryawan() {
        return """
               INSERT INTO USERS (
               NAMA_LENGKAP,
               TANGGAL_LAHIR,
               GENDER,
               ALAMAT,
               EMAIL,
               NO_TELP,
               TIPE_AKUN,
               USERNAME, PASSWORD) VALUES (?,?,?,?,?,?,?,?,?)""";
    }
}
