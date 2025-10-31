
import React, { useState } from "react";
import { createOwner } from "../api";

const CreateAccount: React.FC = () => {
    const [firstname, setFirstname] = useState("");
    const [surname, setSurname] = useState("");
    const [email, setEmail] = useState("");
    const [phonenumber, setPhonenumber] = useState("");

    return (
        <div style={{ 
            position: "relative",  
            height: "100vh",
            paddingTop: 0,
            paddingLeft: 8, }}>
            <h1>Create Account</h1>
            <p>Please enter your details and you'll be able to register a new account.</p>

            <div style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "flex-start",
                gap: "8px"
            }}>
                <input
                    type="text"
                    value={firstname}
                    placeholder="firstname"
                    onChange={ (e) => setFirstname(e.target.value.trim()) }
                    style={{
                        width: "100",
                        padding: "2px 2px"
                    }}
                />
                <input
                    type="text"
                    value={surname}
                    placeholder="surname"
                    onChange={ (e) => setSurname(e.target.value.trim()) }
                    style={{
                        width: "100",
                        padding: "2px 2px"
                    }}
                />
                <input
                    type="text"
                    value={email}
                    placeholder="email"
                    onChange={ (e) => setEmail(e.target.value.trim()) }
                    style={{
                        width: "100",
                        padding: "2px 2px"
                    }}
                />
                <input
                    type="text"
                    value={phonenumber}
                    placeholder="phonenumber"
                    onChange={ (e) => setPhonenumber(e.target.value.trim()) }
                    style={{
                        width: "100",
                        padding: "2px 2px"
                    }}
                />
                <button
                    onClick={async () => {
                        try { 
                            const res = await createOwner({
                                firstname: firstname,
                                surname: surname,
                                email: email,
                                phonenumber: phonenumber
                            }); 
                            alert(`Owner created successfully! ID: ${res.ownerId}`);
                        } catch (err: any) {
                            alert(`Failed to create owner! ${err.message}`)
                        }
                    }}>
                    Create Account
                </button>
            </div>
            
        </div>
    );
};

export default CreateAccount;


