import { ComponentType, useEffect } from "react";
import { useSelector } from "react-redux";
import { RootState } from "../store/store"; // Redux store의 RootState를 가져옵니다.
import { useRouter } from "next/navigation";

const usePrivateRoute = (WrappedComponent: ComponentType) => {
  const isLoggedIn = useSelector((state: RootState) => state.user.isLoggedIn);
  const router = useRouter();

  useEffect(() => {
    if (isLoggedIn) {
      router.replace("/");
    }
  }, [isLoggedIn, router]);

  return isLoggedIn ? null : WrappedComponent;
};

const PublicRoute = (WrappedComponent: ComponentType) => {
  const ComponentWithPrivateRoute = (props: any) => {
    const ComponentOrNull = usePrivateRoute(WrappedComponent);
    // 컴포넌트가 null이 아닌 경우에만 렌더링합니다.
    return ComponentOrNull ? <ComponentOrNull {...props} /> : null;
  };

  // 고차 컴포넌트에 이름을 지정합니다.
  ComponentWithPrivateRoute.displayName = `PrivateRoute(${getDisplayName(
    WrappedComponent
  )})`;

  return ComponentWithPrivateRoute;
};

function getDisplayName(WrappedComponent: ComponentType) {
  return WrappedComponent.displayName || WrappedComponent.name || "Component";
}

export default PublicRoute;
