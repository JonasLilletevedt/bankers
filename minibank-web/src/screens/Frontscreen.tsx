import React, { use, useState } from "react";
import { useNavigate } from "react-router-dom";

const Frontscreen: React.FC = () => {
    
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("")
    const navigate = useNavigate();

    const handleLogin = () => {
        if (email === "") {
            setMessage("Enter email");
            return;
        }
        if (email === "test@bankers.com") {
            /* TODO: handle login */
            alert("TODO: Login successful");
        } else {
            alert("TODO: Wrong email. Please try again.\nIf you do not oawn an account, you can make one below.");
        }
    }

    const handleCreateAccount = () => {
        navigate("/create-account");
    }

    return (
        <div
        style={{
            position: "relative",  
            height: "100vh",
            paddingTop: 0,
            paddingLeft: 8,
        }}
        >
        <h1>Welcome to Bankers</h1>
        <p>The best bank on the world wide web.</p>

        {/* Login input / Create Account */}
        <div
            style={{
                position: "fixed",
                top: "20px",
                right: "20px",
            }}
            >
                <label className=""> Log in </label>
                <input
                    type="text"
                    value={email}
                    placeholder="Enter email"
                    onChange={(e) => setEmail(e.target.value.trim())}
                    style={{
                        width: "100%",
                        padding: "5px 5px"
                    }}
                />

                {/* Create an account */}
                <button
                    onClick={handleCreateAccount}
                    style={{
                        background: "none",
                        border: "none",
                        textDecoration: "underline",
                        cursor: "pointer",
                        padding: 0,
                        fontSize: 14,
                    }}>Create an account</button>
                
                {/* Handle Login */ }
                <button
                    onClick={handleLogin}
                    style={{
                        width: "100%",
                        color: "white",
                        padding: "4px",
                        border: "none"
                    }}
                > Log in</button>
        </div>

        </div>
  );
};

export default Frontscreen;
