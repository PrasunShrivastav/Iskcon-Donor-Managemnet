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
    TextField,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Skeleton,
    Fab,
    Snackbar,
    Button,
    Alert
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import dayjs from 'dayjs';
import EditCategoryDialog from './EditCategoryDialog';
import CreateCategoryDialog from './CreateCategoryDialog';
import DeleteConfirmDialog from './DeleteConfirmDialog';
import { getOnlyCategories, getAllCategories } from '../api/category';

const imageAPI = 'http://localhost:8080'

const columns = [
  { id: 'id', label: 'ID', minWidth: 100 },
  { id: 'name', label: 'Name', minWidth: 150 },
  { id: 'parent_id', label: 'Parent ID', minWidth: 100 },
  { id: 'description', label: 'Description', minWidth: 200 },
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

export default function Category() {
  const [search, setSearch] = useState('');
  const [allCategories, setAllCategories] = useState([]);
  const [filteredCategories, setFilteredCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [anchorEl, setAnchorEl] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [createOpen, setCreateOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const openPopover = Boolean(anchorEl);
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

  useEffect(() => {
    fetchCategories();
  }, []);

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

  const fetchCategories = async () => {
    setLoading(true);
    try {
      const getCategories = await getAllCategories();
      console.log(getCategories);
      setAllCategories(getCategories);
      setFilteredCategories(getCategories)
      setLoading(false)
    } catch (error) {
      console.log(error);
    }
  };

  const handleSearch = () => {
    const filtered = allCategories.filter((row) => {
      return search
        ? Object.values(row).some((val) =>
            (val ?? '').toString().toLowerCase().includes(search.toLowerCase())
          )
        : true;
    });
    setFilteredCategories(filtered);
  };

  const showSnackbar = (message, severity = 'success') => {
    setSnackbar({ open: true, message, severity });
  };

  const handleRowClick = (event, row) => {
    setAnchorEl(event.currentTarget);
    setSelectedCategory(row);
  };

  const handleClosePopover = () => {
    setAnchorEl(null);
    setSelectedCategory(null);
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
          Loading Categories...
        </Typography>
        {[1, 2, 3, 4, 5].map((i) => (
          <Box key={i} sx={{ mb: 2 }}>
            <Skeleton variant="rectangular" height={60} animation="wave" />
          </Box>
        ))}
      </Box>
    );
  }

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
      <Typography variant="h5" gutterBottom>Category List</Typography>

      <Box display="flex" flexWrap="wrap" alignItems="center" gap={2} mt={2}>
            <TextField
              size="small"
              label="Search Anything"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              sx={{ minWidth: 150 }}
            />
            <Button
              size="small"
              variant="contained"
              onClick={handleSearch}
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
                {filteredCategories.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row, idx) => (
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
                          '-'
                        ) : column.format ? (
                          highlightMatch(column.format(value), search)
                        ) : (
                          highlightMatch(String(value), search)
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
            count={filteredCategories.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            sx={{position: 'absolute'}}
          />
        </Paper>

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
      <EditCategoryDialog
        open={editOpen}
        category={selectedCategory}
        onClose={() => setEditOpen(false)}
        onUpdated={fetchCategories}
      />

      {/* Delete Confirmation Dialog */}
      <DeleteConfirmDialog
        open={deleteOpen}
        category={selectedCategory}
        onClose={() => setDeleteOpen(false)}
        onDeleted={fetchCategories}
      />

        {/* Create Modal */}
        <CreateCategoryDialog
        open={createOpen}
        onClose={() => setCreateOpen(false)}
        onUpdated={fetchCategories}
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
