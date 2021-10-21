using Microsoft.AspNetCore.Cors;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using TicketRegistrationCoreApi.Models;

namespace TicketRegistrationCoreApi.Controllers
{
    [Route("api/[controller]")]
    [EnableCors("AllowAllOrigins")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly IConfiguration _config;

        public UserController(IConfiguration config)
        {
            _config = config;
        }

        [Route("Register")]
        [HttpPost]
        public ResponseModel Register([FromBody] UserRegistrationModel model)
        {
            try
            {
                var connectionString = _config.GetValue<string>("AppSettings:ConnectionString");
                var connection = new MySqlConnection(connectionString);
                connection.Open();
                string checkQuery = $"SELECT * FROM bookingusers WHERE Email = '{model.Email}'";
                MySqlCommand checkCmd = new MySqlCommand(checkQuery, connection);
                MySqlDataReader dataReader = checkCmd.ExecuteReader();
                bool emailExist = false;

                while (dataReader.Read())
                {
                    emailExist = true;
                    break;
                }
                dataReader.Close();
                if (emailExist)
                {
                    connection.Close();
                    return new ResponseModel
                    {
                        Status = 0,
                        Message = "User already exists!"
                    };
                }

                string query = "INSERT INTO bookingusers (Name,Age,Address,Email,PhoneNo,Password) VALUES"
                    + $"('{model.Name}', {model.Age}, '{model.Address}','{model.Email}','{model.PhoneNo}','{model.Password}')";
                MySqlCommand cmd = new MySqlCommand(query, connection);
                cmd.ExecuteNonQuery();
                connection.Close();
                return new ResponseModel
                {
                    Status = 1,
                    Data = true,
                    Message = "Success"
                };
            }
            catch (Exception ex)
            {
                throw ex;
            }

        }

        [Route("Login")]
        [HttpPost]
        public ResponseModel Login([FromBody] LoginRequestModel model)
        {
            try
            {
                var connectionString = _config.GetValue<string>("AppSettings:ConnectionString");
                var connection = new MySqlConnection(connectionString);
                connection.Open();
                string query = $"SELECT * FROM bookingusers WHERE Email = '{model.Email}' AND Password = '{model.Password}'";
                MySqlCommand cmd = new MySqlCommand(query, connection);
                MySqlDataReader dataReader = cmd.ExecuteReader();
                bool loginSuccess = false;
                LoginResponseModel result = new LoginResponseModel();

                while (dataReader.Read())
                {
                    loginSuccess = true;
                    result.Id = Convert.ToInt32(dataReader["Id"].ToString());
                    result.Name = dataReader["Name"].ToString();
                    break;
                }

                dataReader.Close();
                connection.Close();
                if (loginSuccess)
                {
                    return new ResponseModel
                    {
                        Status = 1,
                        Data = result,
                        Message = "Success"
                    };
                }
                else
                {
                    return new ResponseModel
                    {
                        Status = 0,
                        Message = "Invalid Credentials!"
                    };
                }

            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        [Route("GetUserDetails")]
        [HttpGet]
        public ResponseModel GetUserDetails(int Id)
        {
            try
            {
                var connectionString = _config.GetValue<string>("AppSettings:ConnectionString");
                var connection = new MySqlConnection(connectionString);
                connection.Open();
                string query = $"SELECT * FROM bookingusers WHERE Id = {Id}";
                MySqlCommand cmd = new MySqlCommand(query, connection);
                MySqlDataReader dataReader = cmd.ExecuteReader();
                UserDetailsModel result = new UserDetailsModel();

                while (dataReader.Read())
                {
                    result.Id = Convert.ToInt32(dataReader["Id"].ToString());
                    result.Name = dataReader["Name"].ToString();
                    result.Age = Convert.ToInt32(dataReader["Age"].ToString());
                    result.Address = dataReader["Address"].ToString();
                    result.Email = dataReader["Email"].ToString();
                    result.PhoneNo = dataReader["PhoneNo"].ToString();
                    break;
                }

                dataReader.Close();
                connection.Close();

                return new ResponseModel
                {
                    Status = 1,
                    Data = result,
                    Message = "Success"
                };
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
    }
}
