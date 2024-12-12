import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaftarBarang extends Barang { // Inheritance: Kelas DaftarBarang mewarisi kelas Barang

    // Konstruktor untuk menginisialisasi objek DaftarBarang
    public DaftarBarang(String kodeBarang, String namaBarang, int hargaBarang) {
        super(kodeBarang, namaBarang, hargaBarang); // Memanggil konstruktor parent (Barang) untuk menginisialisasi kodeBarang, namaBarang, dan hargaBarang
    }

    // Metode untuk mendapatkan daftar barang yang tersedia dari database
    public static List<DaftarBarang> getBarangList() {
        List<DaftarBarang> daftarBarang = new ArrayList<>();
        String query = "SELECT kode_barang, nama_barang, harga_barang FROM Barang"; // Query untuk mengambil data dari tabel Barang
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_pbo_2024", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String kodeBarang = rs.getString("kode_barang");
                String namaBarang = rs.getString("nama_barang");
                int hargaBarang = rs.getInt("harga_barang");

                daftarBarang.add(new DaftarBarang(kodeBarang, namaBarang, hargaBarang));
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat mengambil data barang: " + e.getMessage());
        }
        return daftarBarang;
    }

    // Metode untuk validasi barang berdasarkan kode barang
    public static DaftarBarang validasiBarang(String kodeBarang) throws IllegalArgumentException {
        for (DaftarBarang barang : getBarangList()) {
            if (barang.kodeBarang.equalsIgnoreCase(kodeBarang)) { // Menggunakan metode string equalsIgnoreCase untuk membandingkan kode barang
                return barang; // Mengembalikan barang jika ditemukan
            }
        }
        throw new IllegalArgumentException("Kode barang tidak ditemukan di toko!"); // Melempar exception jika barang tidak ditemukan
    }

    // Metode untuk menampilkan daftar barang
    public static void tampilkanDaftarBarang() {
        System.out.println("\nDaftar Barang:");
        for (DaftarBarang barang : getBarangList()) {
            barang.displayInfo(); // Menampilkan informasi barang menggunakan metode displayInfo() dari kelas Barang
            System.out.println("----------------------------");
        }
    }

    // Metode untuk menampilkan informasi barang
    public void displayInfo() {
        System.out.println("Kode Barang : " + kodeBarang); // Menampilkan kode barang
        System.out.println("Nama Barang : " + namaBarang); // Menampilkan nama barang
        System.out.println("Harga Barang: Rp" + hargaBarang); // Menampilkan harga barang
    }
}
