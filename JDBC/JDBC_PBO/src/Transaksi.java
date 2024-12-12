import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaksi extends Barang {
    private int jumlahBeli;

    public Transaksi(String kodeBarang, String namaBarang, int hargaBarang, int jumlahBeli) {
        super(kodeBarang, namaBarang, hargaBarang);
        this.jumlahBeli = jumlahBeli;
    }

    public int hitungTotal() {
        return jumlahBeli * hargaBarang;
    }

    public void displayTransaksi() {
        System.out.println("Barang: " + namaBarang);
        System.out.println("Jumlah Beli: " + jumlahBeli);
        System.out.println("Total Harga: Rp " + hitungTotal());
    }

    public void simpanTransaksi() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO transaksi (kode_barang, nama_barang, harga_barang, jumlah_beli) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, getKodeBarang());
            stmt.setString(2, getNamaBarang());
            stmt.setInt(3, getHargaBarang());
            stmt.setInt(4, jumlahBeli);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saat menyimpan transaksi: " + e.getMessage());
        }
    }
}
