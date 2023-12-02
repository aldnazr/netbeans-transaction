package TokoHp.Views;

import TokoHp.Objects.Variable;
import java.awt.Color;
import java.sql.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class RiwayatTransaksi extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement preparedStatement;
    String sql;
    ResultSet resultSet;
    Statement statement;
    CallableStatement callableStatement;
    Object[] tableColumn = {"ID TRANSAKSI", "KASIR", "NAMA_PELANGGAN", "WAKTU", "BRAND", "MODEL", "HARGA PRODUK", "JUMLAH BELI", "SUBTOTAL", "TOTAL"};
    DefaultTableModel tableModel = new DefaultTableModel(tableColumn, 0);

    public RiwayatTransaksi() {
        initComponents();
        init();
    }

    private void init() {
        setClosable(true);
        connection = Variable.koneksi();
        read();
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setLabelFont(jLabel8);
        getTransaksi();
        getTerjual();
    }

    private void getTransaksi() {
        try {
            sql = "{? = call transaksiHariIni}";
            callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();
            labelTransaksi.setText(String.valueOf(callableStatement.getInt(1)));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void getTerjual() {
        try {
            sql = "{? = call terjualHariIni}";
            callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();
            labelProdukTerjual.setText(String.valueOf(callableStatement.getInt(1)));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void read() {
        try {
            sql = Variable.sqlRiwayatTransaksi;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + tfPencarian.getText() + "%");
            preparedStatement.setString(2, "%" + tfPencarian.getText().toLowerCase() + "%");
            preparedStatement.setString(3, "%" + tfPencarian.getText().toLowerCase() + "%");
            preparedStatement.setString(4, "%" + tfPencarian.getText() + "%");

            resultSet = preparedStatement.executeQuery();
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] data = {
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getTimestamp(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getInt(7),
                    resultSet.getInt(8),
                    resultSet.getInt(9),
                    resultSet.getInt(10)
                };
                tableModel.addRow(data);
            }
            tableRiwayat.setModel(tableModel);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableRiwayat = new javax.swing.JTable();
        tfPencarian = new javax.swing.JTextField();
        card1 = new TokoHp.Component.Card();
        labelTransaksi = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        card2 = new TokoHp.Component.Card();
        labelProdukTerjual = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1200, 740));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("RIWAYAT TRANSAKSI");

        tableRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableRiwayat);

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        card1.setForeground(new java.awt.Color(10, 132, 255));
        card1.setPreferredSize(new java.awt.Dimension(170, 82));

        labelTransaksi.setFont(new java.awt.Font("Rubik Medium", 1, 20)); // NOI18N
        labelTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        labelTransaksi.setText("10");

        jLabel2.setFont(new java.awt.Font("Rubik Medium", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Transaksi hari ini");

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(labelTransaksi))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(labelTransaksi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(17, 17, 17))
        );

        card2.setForeground(new java.awt.Color(94, 92, 230));

        labelProdukTerjual.setFont(new java.awt.Font("Rubik Medium", 1, 20)); // NOI18N
        labelProdukTerjual.setForeground(new java.awt.Color(255, 255, 255));
        labelProdukTerjual.setText("10");

        jLabel4.setFont(new java.awt.Font("Rubik Medium", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Barang terjual hari ini");

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(labelProdukTerjual))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(labelProdukTerjual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1176, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        read();
    }//GEN-LAST:event_tfPencarianKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private TokoHp.Component.Card card1;
    private TokoHp.Component.Card card2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelProdukTerjual;
    private javax.swing.JLabel labelTransaksi;
    private javax.swing.JTable tableRiwayat;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
