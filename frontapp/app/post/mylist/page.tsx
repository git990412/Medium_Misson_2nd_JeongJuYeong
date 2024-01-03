import PostTable from "@/components/PostTable";
import PrivateRoute from "@/components/PrivateRoute";

const Page = () => {
  return <PostTable url="/posts/myList" />;
};

export default PrivateRoute(Page);
