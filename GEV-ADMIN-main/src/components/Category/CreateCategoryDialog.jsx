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

import axios from 'axios';
import { createCategory } from '../api/category';

export default function CreateCategoryDialog({ open, onClose, onCreated }) {
    const [form, setForm] = useState({ name: '', description: '', parent_id: '' });
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
      data.append('name', form.name);
      data.append('description', form.description);
      data.append('parent_id', form.parent_id);
      if (image) data.append('image', image);
      if (imageSm) data.append('image_sm', imageSm);

      const newCategory = await createCategory(data);

      if(newCategory.ok){
        console.log("new category");
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
      <DialogTitle>Create New Category</DialogTitle>
      <DialogContent>
        <Box sx={{ mt: 1, display: 'flex', flexDirection: 'column', gap: 2 }}>
          <TextField
            name="name"
            label="Name"
            fullWidth
            value={form.name}
            onChange={handleChange}
          />
          <TextField
            name="description"
            label="Description"
            fullWidth
            multiline
            rows={3}
            value={form.description}
            onChange={handleChange}
          />
          <TextField
            name="parent_id"
            label="Parent ID"
            fullWidth
            value={form.parent_id}
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