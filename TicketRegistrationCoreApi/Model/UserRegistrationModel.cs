﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TicketRegistrationCoreApi.Models
{
    public class UserRegistrationModel
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int Age { get; set; }
        public string Address { get; set; }
        public string Email { get; set; }
        public string PhoneNo { get; set; }
        public string Password { get; set; }
    }
}