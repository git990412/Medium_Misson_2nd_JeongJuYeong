"use client";

import { instance } from "@/config/axiosConfig";
import { login } from "@/store/userSlice";
import { Spinner } from "@nextui-org/react";
import { useSearchParams } from "next/navigation";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { useDispatch } from "react-redux";

const Page = ({ params }: { params: { slug: string } }) => {
  const searchParams = useSearchParams();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const router = useRouter();
  const dispatch = useDispatch();

  useEffect(() => {
    if (params.slug === "kakao") {
      const code = searchParams.get("code");

      instance
        .post("/auth/kakao", {
          clientId: process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID,
          redirectUri: process.env.NEXT_PUBLIC_KAKAO_CALLBACK_URL,
          grantType: "authorization_code",
          code: code,
        })
        .then((res) => {
          dispatch(login((res.data as any).username));
          router.push("/");
        });
    }
  }, []);
  return (
    <div className={"mt-20 flex justify-center items-center"}>
      <Spinner />
    </div>
  );
};

export default Page;
