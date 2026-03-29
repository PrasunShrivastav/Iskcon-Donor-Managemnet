import React, { useEffect, useState } from 'react';
import {
  Box,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Skeleton,
  Snackbar,
  Alert,
  Button,
  TextField,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import { getAllDonations, getfilteredDonations } from '../api/donations';

const columns = [
  { id: 'id', label: 'ID', minWidth: 50 },
  { id: 'donor', label: 'Donor ID', minWidth: 90, format: (value) => value?.id ?? 'N/A' },
  { id: 'donor', label: 'First Name', minWidth: 100, format: (value) => `${value?.first_name ?? ''}`.trim() || 'N/A' },
  { id: 'donor', label: 'Last Name', minWidth: 100, format: (value) => `${value?.last_name ?? ''}`.trim() || 'N/A' },
  { id: 'donor', label: 'Contact', minWidth: 100, format: (value) => `${value?.contact_no ?? ''}`.trim() || 'N/A' },
  { id: 'donor', label: 'Email', minWidth: 100, format: (value) => `${value?.email ?? ''}`.trim() || 'N/A' },
  { id: 'causeName', label: 'Cause', minWidth: 150 },
  { id: 'subCategoryName', label: 'Sub Category', minWidth: 150 },
  { id: 'categoryName', label: 'Category', minWidth: 50 },
  { id: 'quantity', label: 'Quantity', minWidth: 90 },
  { id: 'amount', label: 'Amount', minWidth: 50 },
  { id: 'receipt_id', label: 'Receipt ID', minWidth: 100 },
  { id: 'address_line_1', label: 'Address Line 1', minWidth: 200 },
  { id: 'address_line_2', label: 'Address Line 2', minWidth: 200 },
  { id: 'city', label: 'City', minWidth: 100 },
  { id: 'state', label: 'State', minWidth: 100 },
  { id: 'pincode', label: 'Pincode', minWidth: 100 },
  { id: 'created_at', label: 'Created At', minWidth: 100, format: (value) => dayjs(value).format('DD-MM-YYYY HH:mm:ss') },
  { id: 'updated_at', label: 'Updated At', minWidth: 100, format: (value) => dayjs(value).format('DD-MM-YYYY HH:mm:ss') },
  { id: 'updated_by_user_id', label: 'Updated By', minWidth: 100 },
  { id: 'deleted_at', label: 'Deleted At', minWidth: 100, format: (value) => dayjs(value).format('DD-MM-YYYY HH:mm:ss') },
  { id: 'deleted_by_user_id', label: 'Deleted By', minWidth: 100 },
];

export default function Donations() {
  const [loading, setLoading] = useState(true);
  const [filteredDonations, setFilteredDonations] = useState([]);
  const [filters, setFilters] = useState({
    global: '',
    causeName: '',
    subCategory: '',
    categoryName: '',
    donorName: '',
    contact_no: '',
    email: '',
    createdFrom: null,
    createdTo: null,
    page: 0,
    size: 10
  });
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalCount, setTotalCount] = useState(0);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  const showSnackbar = (message, severity = 'success') => {
    setSnackbar({ open: true, message, severity });
  };

  const handleChangePage = async (event, newPage) => {
    setPage(newPage);
    const updatedFilters = { ...filters, page: newPage, size: rowsPerPage };
    setFilters(updatedFilters);
    await searchDonations(updatedFilters);
  };
  
  const handleChangeRowsPerPage = async (event) => {
    const newSize = +event.target.value;
    setRowsPerPage(newSize);
    setPage(0);
    const updatedFilters = { ...filters, page: 0, size: newSize };
    setFilters(updatedFilters);
    await searchDonations(updatedFilters);
  };

  const fetchDonations = async () => {
    try {
      const getDonations = await getAllDonations();
      if (getDonations.length > 0) {
        setFilteredDonations(getDonations);
        setTotalCount(getDonations.length);
      }
    } catch (error) {
      console.error("Error fetching donations:", error);
    } finally {
      setLoading(false);
    }
  };

  const searchDonations = async () => {
    setLoading(true);
    try {
      console.log(filters);
      const getDonationsFiltered = await getfilteredDonations(filters);
      console.log("getDonationsFiltered");
      console.log(getDonationsFiltered.content);
      setTotalCount(getDonationsFiltered.totalElements);
      if (getDonationsFiltered) {
        setFilteredDonations(getDonationsFiltered.content);
      } else {
        fetchDonations();
      }
    } catch (error) {
      console.error("Error fetching donations:", error);
      showSnackbar('Failed to fetch filtered donations', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDonations();
  }, [page, rowsPerPage]);

  const highlightMatch = (text, query) => {
    if (!query || typeof text !== 'string') return text;
    const regex = new RegExp(`(${query})`, 'gi');
    const parts = text.split(regex);
    return parts.map((part, i) =>
      part.toLowerCase() === query.toLowerCase() ? (
        <mark key={i} style={{ backgroundColor: '#FFFF00', padding: '0 2px' }}>{part}</mark>
      ) : (
        part
      )
    );
  };

    if (loading) {
      return (
        <Box sx={{ p: 3 }}>
          <Typography variant="h5" gutterBottom>
            Loading Donations...
          </Typography>
          {[1, 2, 3, 4, 5].map((i) => (
            <Box key={i} sx={{ mb: 2 }}>
              <Skeleton variant="rectangular" height={60} animation="wave" />
            </Box>
          ))}
        </Box>
      );
    }

  return (
    <Box>
      <Typography variant="h5" gutterBottom>Donations List</Typography>

   <Box display="flex" flexWrap="wrap" alignItems="center" gap={2} mb={2}>
         <DatePicker
           label="Created From"
           value={filters.createdFrom}
           onChange={(newValue) => setFilters({ ...filters, createdFrom: newValue })}
           slotProps={{ textField: { size: 'small' } }}
         />
         <DatePicker
           label="Created To"
           value={filters.createdTo}
           onChange={(newValue) => setFilters({ ...filters, createdTo: newValue })}
           slotProps={{ textField: { size: 'small' } }}
         />
        <TextField
          size="small"
          label="Cause"
          value={filters.causeName}
          onChange={(e) => setFilters({ ...filters, causeName: e.target.value })}
          sx={{ minWidth: 150 }}
        />
        <TextField
          size="small"
          label="Sub Category"
          value={filters.subCategory}
          onChange={(e) => setFilters({ ...filters, subCategory: e.target.value })}
          sx={{ minWidth: 150 }}
        />
        <TextField
          size="small"
          label="Category ID"
          type="number"
          value={filters.categoryName}
          onChange={(e) => setFilters({ ...filters, categoryName: e.target.value })}
          sx={{ minWidth: 120 }}
        />
       </Box>

       <Box display="flex" flexWrap="wrap" alignItems="center" gap={2} mb={2}>
        <TextField
          size="small"
          label="Donor Name"
          value={filters.donorName}
          onChange={(e) => setFilters({ ...filters, donorName: e.target.value })}
          sx={{ minWidth: 150 }}
        />
         <TextField
          size="small"
          label="Contact No"
          value={filters.contact_no}
          onChange={(e) => setFilters({ ...filters, contact_no: e.target.value })}
          sx={{ minWidth: 150 }}
        />
        <TextField
          size="small"
          label="Email"
          value={filters.email}
          onChange={(e) => setFilters({ ...filters, email: e.target.value })}
          sx={{ minWidth: 150 }}
        />
          <Button
          size="small"
          variant="contained"
          onClick={searchDonations}
        >
          Search
        </Button>
        </Box>
      

      <Paper sx={{ width: '100%', overflow: 'auto' }}>
        <TableContainer>
          <Table stickyHeader>
            <TableHead>
              <TableRow>
                {columns.map((column) => (
                  <TableCell key={column.id} style={{ minWidth: column.minWidth }}>
                    {column.label}
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredDonations.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row, idx) => (
                <TableRow
                  hover
                  key={idx}
                  sx={{ cursor: 'pointer' }}
                >
                  {columns.map((column) => {
                    const value = row[column.id];
                    return (
                      <TableCell key={column.id}>
                        {value === null || value === undefined || value === '' ? (
                          ''
                        ) : column.format ? (
                          highlightMatch(column.format(value, row), filters.global)
                        ) : (
                          highlightMatch(String(value), filters.global)
                        )}
                      </TableCell>
                    );
                  })}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          component="div"
          count={totalCount}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          sx={{position: 'absolute'}}
        />
      </Paper>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={4000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert severity={snackbar.severity} onClose={() => setSnackbar({ ...snackbar, open: false })}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
