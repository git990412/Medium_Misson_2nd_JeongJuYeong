import { instance } from "@/config/axiosConfig";
import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";

interface UserState {
  isLoggedIn: boolean;
  username: string | null;
}

const initialState: UserState = {
  isLoggedIn: false,
  username: null,
};

export const loginAsync = createAsyncThunk(
  'user/loginAsync',
  async ({ username, password }: { username: string; password: string }) => {
    const response = await instance.post('members/login', { username, password });
    return response.data;
  }
);

export const logoutAsync = createAsyncThunk(
  'user/logoutAsync',
  async () => {
    const response = await instance.post('members/logout');
    return response.data;
  }
);

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(loginAsync.fulfilled, (state, action) => {
      state.isLoggedIn = true;
      state.username = action.payload;
    });
    builder.addCase(logoutAsync.fulfilled, (state) => {
      state.isLoggedIn = false;
      state.username = null;
    });
  },
});

export default userSlice.reducer;
