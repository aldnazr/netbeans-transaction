package TokoHp.Views;

import TokoHp.Objects.Variable;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class RiwayatTransaksi extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement preparedStatement;
    String sql;
    ResultSet resultSet;
    CallableStatement callableStatement;
    Object[] tableColumn = {"ID TRANSAKSI", "KASIR", "NAMA PELANGGAN", "WAKTU", "BRAND", "MODEL", "HARGA PRODUK", "JUMLAH BELI", "SUBTOTAL", "TOTAL"};
    DefaultTableModel tableModel = new DefaultTableModel(tableColumn, 0);

    public RiwayatTransaksi() {
        initComponents();
        init();
    }

    private void init() {
        setClosable(true);
        connection = Variable.koneksi();
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setSearchFieldIcon(tfPencarian);
        Variable.setFontTitle(jLabel8);
        getTransaksi();
        getTerjual();
        setTableData();
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

    private void setTableData() {
        try {
            sql = switch (cbFilterDate.getSelectedIndex()) {
                case 1 ->
                    Variable.riwayatTransaksi("(TRUNC(T.TANGGAL) >= SYSDATE - 7) AND");
                case 2 ->
                    Variable.riwayatTransaksi("(TRUNC(T.TANGGAL) >= SYSDATE - 14) AND");
                case 3 ->
                    Variable.riwayatTransaksi("(TRUNC(T.TANGGAL) >= SYSDATE - 30) AND");
                default ->
                    Variable.riwayatTransaksi("");
            };
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
        jLabel1 = new javax.swing.JLabel();
        card2 = new TokoHp.Component.Card();
        labelProdukTerjual = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbFilterDate = new javax.swing.JComboBox<>();

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

        labelTransaksi.setFont(new java.awt.Font("Rubik Medium", 1, 22)); // NOI18N
        labelTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        labelTransaksi.setText("10");

        jLabel1.setFont(new java.awt.Font("Rubik Medium", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transaksi hari ini");

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(labelTransaksi))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelTransaksi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(17, 17, 17))
        );

        card2.setForeground(new java.awt.Color(94, 92, 230));

        labelProdukTerjual.setFont(new java.awt.Font("Rubik Medium", 1, 22)); // NOI18N
        labelProdukTerjual.setForeground(new java.awt.Color(255, 255, 255));
        labelProdukTerjual.setText("10");

        jLabel2.setFont(new java.awt.Font("Rubik Medium", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Barang terjual hari ini");

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(labelProdukTerjual))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(labelProdukTerjual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(17, 17, 17))
        );

        cbFilterDate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tanggal", "7 Hari", "14 Hari", "30 Hari" }));
        cbFilterDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFilterDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1176, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cbFilterDate, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(card1, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                    .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFilterDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfPencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPencarianKeyReleased
        setTableData();
    }//GEN-LAST:event_tfPencarianKeyReleased

    private void cbFilterDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFilterDateActionPerformed
        setTableData();
    }//GEN-LAST:event_cbFilterDateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private TokoHp.Component.Card card1;
    private TokoHp.Component.Card card2;
    private javax.swing.JComboBox<String> cbFilterDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelProdukTerjual;
    private javax.swing.JLabel labelTransaksi;
    private javax.swing.JTable tableRiwayat;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
