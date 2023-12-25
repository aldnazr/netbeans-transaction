package TokoHp.Views;

import TokoHp.Objects.Variable;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class RiwayatTransaksi extends javax.swing.JInternalFrame {

    Connection connection;
    PreparedStatement preparedStatement;
    String sql;
    ResultSet resultSet;
    int executeResult;
    CallableStatement callableStatement;
    DefaultTableModel tableModel;

    public RiwayatTransaksi() {
        initComponents();
        init();
    }

    private void init() {
        connection = Variable.koneksi();
        Variable.setPlaceholderTextfield(tfPencarian, "Cari");
        Variable.setSearchFieldIcon(tfPencarian);
        setTableData();
        getTransaksi();
        getTerjual();
    }

    private void getTransaksi() {
        try {
            sql = "{? = call transaksiHariIni}";
            callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();
            executeResult = callableStatement.getInt(1);

            labelTransaksi.setText(String.valueOf(executeResult));
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
            executeResult = callableStatement.getInt(1);

            labelProdukTerjual.setText(String.valueOf(executeResult));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void setTableData() {
        tableModel = (DefaultTableModel) tableRiwayat.getModel();
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
                    Variable.stringToNumber(String.valueOf(resultSet.getInt(7))),
                    resultSet.getInt(8),
                    Variable.stringToNumber(String.valueOf(resultSet.getInt(9))),
                    Variable.stringToNumber(String.valueOf(resultSet.getInt(10)))
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tableRiwayat = new javax.swing.JTable();
        tfPencarian = new javax.swing.JTextField();
        card1 = new TokoHp.Component.Card();
        labelTransaksi = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbFilterDate = new javax.swing.JComboBox<>();
        card3 = new TokoHp.Component.Card();
        labelProdukTerjual = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1200, 740));

        tableRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Transaksi", "Kasir", "Nama Pelanggan", "Waktu", "Brand", "Model", "Harga Per-Item", "Jumlah Pemelian", "Subtotal", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableRiwayat);
        if (tableRiwayat.getColumnModel().getColumnCount() > 0) {
            tableRiwayat.getColumnModel().getColumn(0).setPreferredWidth(80);
            tableRiwayat.getColumnModel().getColumn(0).setMaxWidth(80);
            tableRiwayat.getColumnModel().getColumn(1).setPreferredWidth(120);
            tableRiwayat.getColumnModel().getColumn(1).setMaxWidth(120);
            tableRiwayat.getColumnModel().getColumn(7).setPreferredWidth(110);
            tableRiwayat.getColumnModel().getColumn(7).setMaxWidth(110);
        }

        tfPencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPencarianKeyReleased(evt);
            }
        });

        card1.setForeground(new java.awt.Color(111, 131, 212));
        card1.setPreferredSize(new java.awt.Dimension(170, 82));

        labelTransaksi.setFont(new java.awt.Font("Rubik Medium", 0, 26)); // NOI18N
        labelTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        labelTransaksi.setText("10");

        jLabel1.setFont(new java.awt.Font("Rubik Medium", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transaksi hari ini");

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTransaksi)
                    .addComponent(jLabel1))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTransaksi)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        cbFilterDate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tanggal", "7 Hari", "14 Hari", "30 Hari" }));
        cbFilterDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFilterDateActionPerformed(evt);
            }
        });

        card3.setForeground(new java.awt.Color(111, 131, 212));
        card3.setPreferredSize(new java.awt.Dimension(170, 82));

        labelProdukTerjual.setFont(new java.awt.Font("Rubik Medium", 0, 26)); // NOI18N
        labelProdukTerjual.setForeground(new java.awt.Color(255, 255, 255));
        labelProdukTerjual.setText("10");

        jLabel3.setFont(new java.awt.Font("Rubik Medium", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Barang terjual hari ini");

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelProdukTerjual)
                    .addComponent(jLabel3))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelProdukTerjual)
                .addContainerGap(24, Short.MAX_VALUE))
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
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(116, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    private TokoHp.Component.Card card3;
    private javax.swing.JComboBox<String> cbFilterDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelProdukTerjual;
    private javax.swing.JLabel labelTransaksi;
    private javax.swing.JTable tableRiwayat;
    private javax.swing.JTextField tfPencarian;
    // End of variables declaration//GEN-END:variables
}
