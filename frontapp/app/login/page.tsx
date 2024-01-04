"use client";
import EmailInput from "@/components/EmailInput";
import PasswordInput from "@/components/PasswordInput";
import PublicRoute from "@/components/PublicRoute";
import { instance } from "@/config/axiosConfig";
import { AppDispatch } from "@/store/store";
import { login } from "@/store/userSlice";
import { Button } from "@nextui-org/react";
import { AxiosError } from "axios";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { useDispatch } from "react-redux";

const Page = () => {
  const dispatch = useDispatch<AppDispatch>();
  const router = useRouter();

  const [loginForm, setLoginForm] = useState({
    email: "",
    password: "",
  });

  const [errors, setErrors] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLoginForm({
      ...loginForm,
      [e.target.name]: e.target.value,
    });

    setErrors({
      ...errors,
      [e.target.name]: "",
    });
  };

  const handleSubmit = async () => {
    instance
      .post("/members/login", loginForm)
      .then((res) => {
        dispatch(login((res.data as any).username));
        router.push("/");
      })
      .catch((err: AxiosError) => {
        setErrors({
          ...errors,
          ...(err.response?.data as any),
        });
      });
  };

  const kakaoLogin = () => {
    window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID}&redirect_uri=${process.env.NEXT_PUBLIC_KAKAO_CALLBACK_URL}&response_type=code`;
  };

  return (
    <div className="flex justify-center">
      <div className="w-3/4 max-w-sm flex flex-col items-center">
        <h1 className="text-4xl font-bold mt-20">Login</h1>
        <form action={handleSubmit} className="w-full">
          <EmailInput
            value={loginForm.email}
            onChange={handleChange}
            className="w-full mt-8"
            isInvalid={errors.email !== ""}
            errorMessage={errors.email}
          />
          <PasswordInput
            className="w-full mt-2"
            value={loginForm.password}
            onChange={handleChange}
            isInvalid={errors.password !== ""}
            errorMessage={errors.password}
            name="password"
            lable="Password"
          />
          <Button className="w-full mt-2" type="submit">
            로그인
          </Button>
        </form>
        <Button
          className={"w-full mt-2"}
          color={"warning"}
          onClick={kakaoLogin}
        >
          카카오로그인
        </Button>
      </div>
    </div>
  );
};

export default PublicRoute(Page);
