import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  InputLabel,
  IconButton
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { createCause } from '../api/cause';

export default function CreateCause({ open, onClose, onCreated }) {
    const [form, setForm] = useState({ display_name: '', default_amount: '', category_id: ''});
    const [image, setImage] = useState(null);
    const [imageSm, setImageSm] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const [imageSmPreview, setImageSmPreview] = useState(null);
    const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleFileChange = (e, type) => {
    const file = e.target.files[0];
    if (!file) return;
  
    if (type === 'image') {
      setImage(file);
      setImagePreview(URL.createObjectURL(file));
    } else if (type === 'imageSm') {
      setImageSm(file);
      setImageSmPreview(URL.createObjectURL(file));
    }
  };


  const resetImage = (type) => {
    if (type === 'image') {
      setImage(null);
      setImagePreview(null);
    } else {
      setImageSm(null);
      setImageSmPreview(null);
    }
  };

  const handleSubmit = async () => {
    setSubmitting(true);
    try {
      const data = new FormData();
      data.append('display_name', form.display_name);
      data.append('default_amount', form.default_amount);
      if (image) data.append('image', image);
      if (imageSm) data.append('image_sm', imageSm);

      const newCause = await createCause(data, form.category_id) 

      if(newCause.ok){
        console.log('new created');
      }

      onClose();
    } catch (error) {
      console.error('Failed to create category:', error);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Create New Cause</DialogTitle>
      <DialogContent>
        <Box sx={{ mt: 1, display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField
            name="display_name"
            label="Name"
            fullWidth
            value={form.display_name}
            onChange={handleChange}
          />
          <TextField
            name="default_amount"
            label="Amount"
            fullWidth
            value={form.default_amount}
            onChange={handleChange}
          />
          <TextField
            name="category_id"
            label="Parent ID"
            fullWidth
            value={form.category_id}
            onChange={handleChange}
          />
        <Box>
          <InputLabel shrink>Upload Image (Full Size)</InputLabel>
          <input type="file" accept="image/*" onChange={(e) => handleFileChange(e, 'image')} />
          {imagePreview && (
            <Box mt={1}>
              <img src={imagePreview} alt="Preview" height="100" style={{ borderRadius: 8 }} />
              <IconButton
                  size="small"
                  onClick={() => resetImage('image')}
                  sx={{ position: 'absolute', top: 0, right: 0 }}
                >
                  <CloseIcon fontSize="small" />
                </IconButton>
            </Box>
            
          )}
        </Box>

        <Box>
          <InputLabel shrink>Upload Image (Small Device)</InputLabel>
          <input type="file" accept="image/*" onChange={(e) => handleFileChange(e, 'imageSm')} />
          {imageSmPreview && (
            <Box mt={1}>
              <img src={imageSmPreview} alt="Small Preview" height="100" style={{ borderRadius: 8 }} />
              <IconButton
                  size="small"
                  onClick={() => resetImage('image')}
                  sx={{ position: 'absolute', top: 0, right: 0 }}
                >
                  <CloseIcon fontSize="small" />
                </IconButton>
            </Box>
          )}
        </Box>

        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={submitting}>Cancel</Button>
        <Button onClick={handleSubmit} disabled={submitting} variant="contained">
          {submitting ? 'Creating...' : 'Create'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}