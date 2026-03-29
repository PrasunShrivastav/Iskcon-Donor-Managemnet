import React, { useEffect, useState } from 'react';
import {
  Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  TablePagination, Popover, List, ListItem, ListItemIcon, ListItemText, Skeleton, Fab, Chip,
  Snackbar, Alert, TextField, Grid,
  Button
} from '@mui/material';
import { getAllDonors, filterDonors } from '../api/donors';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import EditIcon from '@mui/icons-material/Edit';
import EditDonor from './EditDonor';

const columns = [
  { id: 'id', label: 'ID', minWidth: 50 },
  { id: 'first_name', label: 'First Name', minWidth: 150 },
  { id: 'last_name', label: 'Last Name', minWidth: 150 },
  { id: 'email', label: 'Email', minWidth: 100 },
  { id: 'contact_no', label: 'Contact', minWidth: 100 },
  { id: 'pan_number', label: 'PAN No.', minWidth: 100 },
  { id: 'address_line_1', label: 'Address Line 1', minWidth: 200 },
  { id: 'address_line_2', label: 'Address Line 2', minWidth: 200 },
  { id: 'city', label: 'City', minWidth: 100 },
  { id: 'state', label: 'State', minWidth: 100 },
  { id: 'pincode', label: 'Pincode', minWidth: 100 },
  {
    id: 'created_at',
    label: 'Created At',
    minWidth: 200,
    format: value => value ? dayjs(value).format('DD-MM-YYYY HH:mm:ss') : ''
  },
  {
    id: 'updated_at',
    label: 'Updated At',
    minWidth: 200,
    format: value => value ? dayjs(value).format('DD-MM-YYYY HH:mm:ss') : ''
  },
  { id: 'updated_by_user_id', label: 'Updated By', minWidth: 100 },
  {
    id: 'deleted_at',
    label: 'Deleted At',
    minWidth: 100,
    format: value => value ? dayjs(value).format('DD-MM-YYYY HH:mm:ss') : ''
  },
  { id: 'deleted_by_user_id', label: 'Deleted By', minWidth: 100 },
];

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

export default function Donors() {
  const [filteredDonors, setFilteredDonors] = useState([]);
  const [filters, setFilters] = useState({
    createdFrom: null,
    createdTo: null,
    global: '',
    totalElements: 123,
    totalPages: 13,
    page: 0,
    size: 10
  });
  const [loading, setLoading] = useState(true);
  const [selectedDonor, setSelectedDonor] = useState(null);
  const [anchorEl, setAnchorEl] = useState(null);
  const [editOpen, setEditOpen] = useState(false);
  const [page, setPage] = useState(0);
  const [totalRows, setTotalRows] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const openPopover = Boolean(anchorEl);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  const showSnackbar = (message, severity = 'success') => {
    setSnackbar({ open: true, message, severity });
  };

  const searchDonor = async (newPage = page, newSize = rowsPerPage) => {
    try {
      setLoading(true);
      const payload = {
        global: filters.global,
        createdFrom: filters.createdFrom ? dayjs(filters.createdFrom).format("YYYY-MM-DD") : null,
        createdTo: filters.createdTo ? dayjs(filters.createdTo).format("YYYY-MM-DD") : null,
        page: newPage,
        size: newSize
      };
      console.log(payload);
      const response = await filterDonors(payload);
      console.log(response);
      setFilteredDonors(response.content);
      setTotalRows(response.totalElements);
      setPage(response.page);
    } catch (err) {
      console.error('Search failed:', err);
      showSnackbar('Failed to fetch filtered donors', 'error');
    } finally {
      setLoading(false);
    }
  };
  

  const handleRowClick = (event, row) => {
    setAnchorEl(event.currentTarget);
    setSelectedDonor(row);
  };

  const handleClosePopover = () => {
    setAnchorEl(null);
    setSelectedDonor(null);
  };

  const handleEdit = () => {
    setEditOpen(true);
    handleClosePopover();
  };

  const handleChangePage = async (event, newPage) => {
    await searchDonor(newPage, rowsPerPage);
  };
  
  const handleChangeRowsPerPage = async (event) => {
    const newSize = +event.target.value;
    setRowsPerPage(newSize);
    await searchDonor(0, newSize); // reset to page 0
  };

  const fetchDonors = async () => {
    try {
      const getDonors = await getAllDonors();
      setFilteredDonors(getDonors);
    } catch (error) {
      console.error("Error fetching donors:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDonors();
  }, []);

  if (loading) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h5" gutterBottom>
          Loading Donors...
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
      <Typography variant="h5" gutterBottom>Donors List</Typography>

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
          label="Search Anything"
          value={filters.global}
          onChange={(e) => setFilters({ ...filters, global: e.target.value })}
          sx={{ minWidth: 150 }}
        />
        <Button
          size="small"
          variant="contained"
          onClick={() => searchDonor(0, rowsPerPage)}
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
              {filteredDonors.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row, idx) => (
                <TableRow
                  hover
                  key={idx}
                  onClick={(e) => handleRowClick(e, row)}
                  sx={{ cursor: 'pointer' }}
                >
                  {columns.map((column) => {
                    const value = row[column.id];
                    return (
                      <TableCell key={column.id}>
                        {value === null || value === undefined || value === '' ? (
                          ''
                        ) : column.format ? (
                          highlightMatch(column.format(value), filters.global)
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
          count={totalRows}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          sx={{position: 'absolute'}}
        />
      </Paper>

      <Popover
        open={openPopover}
        anchorEl={anchorEl}
        onClose={handleClosePopover}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
      >
        <List dense>
          <ListItem button onClick={handleEdit}>
            <ListItemIcon><EditIcon /></ListItemIcon>
            <ListItemText primary="Update" />
          </ListItem>
        </List>
      </Popover>

      <EditDonor
        open={editOpen}
        donor={selectedDonor}
        onClose={() => setEditOpen(false)}
        onUpdated={fetchDonors}
      />

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
