import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Login } from "./components/pages/Login/Login";
import { Register } from "./components/pages/Register/Register";
import { HomePage } from "./components/pages/HomePage/HomePage";
import { FrontPage } from "./components/pages/FrontPage/FrontPage";
import { TestPage } from "./components/pages/TestPage/TestPage";
import { Settings } from "./components/pages/Settings/Settings.js";
import { Subscription } from "./components/pages/Subsciption/Subscription.js";
import { Correction } from "./components/pages/Correction/Correction.js";
//import { EditExercisePage } from "./components/pages/EditExercisePage/EditExercisePage";
import { Catalog } from "./components/objects/SVGImages/catalog";
import { UserInterface } from "./UserInterface";
import "./App.css";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<HomePage />} />
        <Route path="/settings" element={<Settings />} />
        <Route path="/pricing" element={<Subscription />} />
        <Route
          path="/user"
          element={
            <UserInterface
              userData={{
                id: 1,
                listExercises: {},
                selectedExercise: "",
                selectedGroup: "all",
              }}
            />
          }
        >
          <Route index element={<FrontPage />} />
          <Route path="test" element={<TestPage />} />
          <Route path="correction" element={<Correction />} />
          {/* <Route path="edit" element={<EditExercisePage />} /> */}
          <Route path="catalog" element={<Catalog />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
