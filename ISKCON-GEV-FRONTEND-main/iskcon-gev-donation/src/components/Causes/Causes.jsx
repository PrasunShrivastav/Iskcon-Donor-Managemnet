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
    Popover,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Skeleton,
    Fab,
    Snackbar,
    Alert
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import dayjs from 'dayjs';
import EditCause from './EditCause';
import CreateCause from './CreateCause';
import DeleteCause from './DeleteCause';
import {getAllCauses} from '../api/cause' ;

const imageAPI = 'http://localhost:8080'

const columns = [
  { id: 'id', label: 'ID', minWidth: 100 },
  { id: 'default_amount', label: 'Amount', minWidth: 100 },
  { id: 'display_name', label: 'Name', minWidth: 150 },
  { id: 'subCategoryName', label: 'Sub Category', minWidth: 150 },
  { id: 'categoryName', label: 'Category ID', minWidth: 100 },
  { id: 'image_url', label: 'Image', minWidth: 200,format: (value) => <img src={imageAPI+value} alt="preview" height={150} style={{ borderRadius: 4 }} />},
  { id: 'image_url_sm', label: 'Image (Small)', minWidth: 200,format: (value) => <img src={imageAPI+value} alt="preview-sm" height={150} style={{ borderRadius: 4 }} />},
  { id: 'deleted', label: 'Deleted?', minWidth: 200 },
  { id: 'created_at', label: 'Created At', minWidth: 170, format: (value) => dayjs(value).format('DD-MM-YYYY HH:mm:ss') },
  { id: 'created_by_user_id', label: 'Created By', minWidth: 100 },
  { id: 'updated_at', label: 'Updated At', minWidth: 170, format: (value) => dayjs(value).format('DD-MM-YYYY HH:mm:ss') },
  { id: 'updated_by_user_id', label: 'Updated By', minWidth: 100 },
  { id: 'deleted_at', label: 'Deleted At', minWidth: 170, format: (value) => dayjs(value).format('DD-MM-YYYY HH:mm:ss') },
  { id: 'deleted_by_user_id', label: 'Deleted By', minWidth: 100 },
];

export default function Causes() {
  const [causes, setCauses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [anchorEl, setAnchorEl] = useState(null);
  const [selectedCause, setSelectedCause] = useState(null);
  const [createOpen, setCreateOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const openPopover = Boolean(anchorEl);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  useEffect(() => {
    fetchCauses();
  }, []);

  const fetchCauses = async () => {
    setLoading(true);
    try {
      const getCauses = await getAllCauses();
      console.log(getCauses);
      setCauses(getCauses)
      setLoading(false)
    } catch (error) {
      console.log(error);
    }
  };

  const showSnackbar = (message, severity = 'success') => {
    setSnackbar({ open: true, message, severity });
  };

  const handleRowClick = (event, row) => {
    setAnchorEl(event.currentTarget);
    setSelectedCause(row);
  };

  const handleClosePopover = () => {
    setAnchorEl(null);
    setSelectedCause(null);
  };

  const handleEdit = () => {
    setEditOpen(true);
    handleClosePopover();
  };

  const handleDelete = () => {
    setDeleteOpen(true);
    handleClosePopover();
  };

  const handleChangePage = (event, newPage) => setPage(newPage);
  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  if (loading) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h5" gutterBottom>
          Loading Causes...
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
      <Typography variant="h5" gutterBottom>Cause List</Typography>

      {loading ? (
        <Box sx={{ p: 3 }}>
          <Typography variant="h6">Loading Causes...</Typography>
          {[...Array(5)].map((_, i) => (
            <Box key={i} sx={{ mb: 2 }}>
              <Skeleton variant="rectangular" height={50} />
            </Box>
          ))}
        </Box>
      ) : (
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
                {causes.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row, idx) => (
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
                          {column.format ? column.format(value) : value || '-'}
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
            count={causes.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            sx={{position: 'absolute'}}
          />
        </Paper>
      )}

        {/* FAB to Create Category */}
        <Fab
            color="primary"
            sx={{ position: 'fixed', bottom: 100, right: 50 }}
            onClick={() => {
            setCreateOpen(true);
            }}
        >
        <AddIcon />
      </Fab>

      {/* Popover with actions */}
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
          <ListItem button onClick={handleDelete}>
            <ListItemIcon><DeleteIcon color="error" /></ListItemIcon>
            <ListItemText primary="Delete" />
          </ListItem>
        </List>
      </Popover>

      {/* Edit Modal */}
      <EditCause
        open={editOpen}
        cause={selectedCause}
        onClose={() => setEditOpen(false)}
        onUpdated={fetchCauses}
      />

      {/* Delete Confirmation Dialog */}
      <DeleteCause
        open={deleteOpen}
        category={selectedCause}
        onClose={() => setDeleteOpen(false)}
        onDeleted={fetchCauses}
      />

        {/* Create Modal */}
        <CreateCause
        open={createOpen}
        onClose={() => setCreateOpen(false)}
        onUpdated={fetchCauses}
      />


      {/* Snackbar Notification */}
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
