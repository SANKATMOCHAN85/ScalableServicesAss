using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TicketRegistrationCoreApi.Models
{
    public class ResponseModel
    {
        public int Status { get; set; }
        public string Message { get; set; }
        public dynamic Data { get; set; }
    }
}