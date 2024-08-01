
const replaceSVGTemplatePlaceholders = (template, data) => {
    return template.replace(/{{(.*?)}}/g, (match, key) => {
      key = key.replace(/^\//, '').replace(/\/$/, '');
      const keys = key.split('/');
      let value = data;
      keys.forEach(k => {
        if (value) {
          value = value[k];
          if (isValidDateTime(value)) {
            value = formatDate(value);
          }
        }
      });
      return value !== undefined ? String(value) : '';
    });
  };
  
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}/${month}/${day}`;
  };
  
  const isValidDateTime = (dateString) => {
    const dateObject = new Date(dateString);
    return (
      dateObject instanceof Date &&
      !isNaN(dateObject) &&
      dateObject.getFullYear() >= 0 &&
      dateObject.getFullYear() <= 9999
    );
  };
  
  module.exports = { replaceSVGTemplatePlaceholders };
