import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Barang { 
    protected String kodeBarang;  // Variabel untuk menyimpan kode barang
    protected String namaBarang;  // Variabel untuk menyimpan nama barang
    protected int hargaBarang;    // Variabel untuk menyimpan harga barang

    // Konstruktor untuk menginisialisasi objek Barang
    public Barang(String kodeBarang, String namaBarang, int hargaBarang) {
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
    }

    // Menambahkan barang baru ke database
    public static void tambahBarang(Barang barang) {
        String query = "INSERT INTO barang (kode_barang, nama_barang, harga_barang) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, barang.kodeBarang);
            stmt.setString(2, barang.namaBarang);
            stmt.setInt(3, barang.hargaBarang);
            stmt.executeUpdate();
            System.out.println("Barang berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Error saat menambahkan barang: " + e.getMessage());
        }
    }
    
    // Mengambil barang berdasarkan kode barang
    public static Barang getBarangByKode(String kodeBarang) {
        String query = "SELECT * FROM barang WHERE kode_barang = ?";
        Barang barang = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, kodeBarang);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                barang = new Barang(kodeBarang, rs.getString("nama_barang"), rs.getInt("harga_barang"));
            }
        } catch (SQLException e) {
            System.out.println("Error saat mengambil barang: " + e.getMessage());
        }
        return barang;
    }

    // Mengupdate barang berdasarkan objek Barang
    public static void updateBarang(Barang barang) {
        String query = "UPDATE barang SET nama_barang = ?, harga_barang = ? WHERE kode_barang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, barang.namaBarang);
            stmt.setInt(2, barang.hargaBarang);
            stmt.setString(3, barang.kodeBarang);
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Barang berhasil diperbarui!");
            } else {
                System.out.println("Barang tidak ditemukan untuk diperbarui.");
            }
        } catch (SQLException e) {
            System.out.println("Error saat mengupdate barang: " + e.getMessage());
        }
    }
    

    // Menghapus barang berdasarkan kode barang
    public static void deleteBarang(String kodeBarang) {
        String query = "DELETE FROM barang WHERE kode_barang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Validasi bahwa barang dengan kodeBarang ada
            if (!barangExists(kodeBarang)) {
                System.out.println("Barang dengan kode " + kodeBarang + " tidak ditemukan.");
                return;
            }
            
            stmt.setString(1, kodeBarang);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Barang berhasil dihapus!");
            } else {
                System.out.println("Barang tidak ditemukan untuk dihapus.");
            }
        } catch (SQLException e) {
            System.out.println("Error saat menghapus barang: " + e.getMessage());
        }
    }
    
    // Metode untuk memeriksa apakah barang ada
    private static boolean barangExists(String kodeBarang) {
        String query = "SELECT COUNT(*) FROM barang WHERE kode_barang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, kodeBarang);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error saat memeriksa keberadaan barang: " + e.getMessage());
        }
        return false;
    }
    

    // Getter dan setter untuk kodeBarang, namaBarang, dan hargaBarang
    public String getKodeBarang() {
        return kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public int getHargaBarang() {
        return hargaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public void setHargaBarang(int hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

        // Metode untuk menampilkan informasi barang
        public void displayInfo() {
            System.out.println("Kode Barang : " + kodeBarang); // Menampilkan kode barang
            System.out.println("Nama Barang : " + namaBarang); // Menampilkan nama barang
            System.out.println("Harga Barang: Rp" + hargaBarang); // Menampilkan harga barang
        }
}
