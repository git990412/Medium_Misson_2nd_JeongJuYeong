import { ComponentType, useEffect } from "react";
import { useSelector } from "react-redux";
import { RootState } from "../store/store";
import { useRouter } from "next/navigation";

// 커스텀 훅으로 변경하여 Hooks 사용 규칙을 준수합니다.
const usePrivateRoute = (WrappedComponent: ComponentType) => {
  const isLoggedIn = useSelector((state: RootState) => state.user.isLoggedIn);
  const router = useRouter();

  useEffect(() => {
    if (!isLoggedIn) {
      router.replace("/");
    }
  }, [isLoggedIn, router]);

  return isLoggedIn ? WrappedComponent : null;
};

const PrivateRoute = (WrappedComponent: ComponentType) => {
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

// 컴포넌트의 displayName을 가져오는 함수
function getDisplayName(WrappedComponent: ComponentType) {
  return WrappedComponent.displayName || WrappedComponent.name || "Component";
}

export default PrivateRoute;
